package com.nhnacademy.bookstore.cart.controller;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.cart.dto.CartDeleteDto;
import com.nhnacademy.bookstore.cart.dto.CartRequestDto;
import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.dto.CartUpdateDto;
import com.nhnacademy.bookstore.cart.entity.CartId;
import com.nhnacademy.bookstore.cart.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    private final RedisTemplate<Object, Object> redisTemplate;

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCart(@RequestHeader(value = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                                         @CookieValue(name = "cartSession", required = false) String cartSessionId) {

        if (customerId == 0) {
            List<CartResponseDto> cart = new ArrayList<>();
            Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(cartSessionId);
            for (Map.Entry<Object, Object> entry : cartMap.entrySet()) {
                BookResponseDto book = bookService.getBookById(Long.parseLong(entry.getKey().toString()));
                String[] values = entry.getValue().toString().split("/");
                cart.add(new CartResponseDto(Long.parseLong(entry.getKey().toString()),
                        book.title(),
                        book.sellingPrice(),
                        Integer.parseInt(values[2])));
            }
            return ResponseEntity.ok(cart);
        }

        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<String> addToCart(HttpServletResponse response,
                                          @CookieValue(name = "cartSession", required = false) String cartSessionId,
                                          @RequestHeader(name = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                          @RequestBody CartRequestDto cartRequestDto) {
        String newCartSessionId;
        int statusCode; //1: 성공, 0: 재고부족, -1: 장바구니에 이미 존재
        if (bookService.getBookRemainQuantity(cartRequestDto.bookId()) - cartRequestDto.quantity() < 0) {
            statusCode = 0;
        } else {
            if (customerId == 0) {
                // 비회원 로직
                if (cartSessionId == null || cartSessionId.isEmpty()) { // 비회원이 처음 장바구니에 상품을 담을 때 (세션이 없을때)
                    newCartSessionId = "cart:" + UUID.randomUUID();
                    redisTemplate.opsForHash().put(newCartSessionId, String.valueOf(cartRequestDto.bookId()), cartRequestDto.title() + "/" + cartRequestDto.sellingPrice() + "/" + cartRequestDto.quantity());
                    return ResponseEntity.status(HttpStatus.CREATED).body(newCartSessionId);
                } else { // 비회원이 장바구니에 상품을 담을때 (세션O)
                    if (redisTemplate.opsForHash().get(cartSessionId, String.valueOf(cartRequestDto.bookId())) == null) {
                        redisTemplate.opsForHash().put(cartSessionId, String.valueOf(cartRequestDto.bookId()), cartRequestDto.title() + "/" + cartRequestDto.sellingPrice() + "/" + cartRequestDto.quantity());
                        statusCode = 1;
                    } else { // 장바구니에 상품이 존재할때
                        statusCode = -1;
                    }

                }
            } else {
                // 회원 로직
                statusCode = cartService.addCart(cartRequestDto, customerId);
            }
        }
        return switch (statusCode) {
            case 1 -> ResponseEntity.status(HttpStatus.CREATED).build();
            case -1 -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            default -> ResponseEntity.status(HttpStatus.CONFLICT).build();
        };
    }

    // DELETE
    @DeleteMapping
    public ResponseEntity<Void> deleteFromCart(@RequestHeader(name = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                            @CookieValue(name = "cartSession", required = false) String cartSessionId,
                                            @RequestBody CartDeleteDto cartDeleteDto) {

        if (customerId == 0) {
            // 비회원
            redisTemplate.opsForHash().delete(cartSessionId, String.valueOf(cartDeleteDto.bookId()));
        }
        cartService.removeCart(new CartId(cartDeleteDto.bookId(), customerId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<CartUpdateDto> updateToCart(@RequestHeader(name = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                             @CookieValue(name = "cartSession", required = false) String cartSessionId,
                                             @RequestBody CartRequestDto cartRequestDto) {

        if (customerId == 0) {
            String[] redisValue = redisTemplate.opsForHash().get(cartSessionId, String.valueOf(cartRequestDto.bookId())).toString().split("/");
            redisValue[2] = String.valueOf(cartRequestDto.quantity());
            redisTemplate.opsForHash().put(cartSessionId, String.valueOf(cartRequestDto.bookId()), redisValue[0] + "/" + redisValue[1] + "/" + redisValue[2]);

        } else {
            cartService.updateCart(cartRequestDto, customerId);
        }
        CartUpdateDto cartUpdateDto = new CartUpdateDto(cartRequestDto.bookId(), cartRequestDto.quantity());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartUpdateDto);
    }
}
