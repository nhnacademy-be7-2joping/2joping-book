package com.nhnacademy.bookstore.cart.controller;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.cart.dto.CartRequestDto;
import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.service.CartService;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RedisTemplate<Object, Object> redisTemplate;

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCart(@RequestHeader(value = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                                         @CookieValue(name = "cartSession", required = false) String cartSessionId) {

        if (customerId == 0) {
            List<CartResponseDto> cart = new ArrayList<>();
            // TODO 비회원 로직, 비회원이면 cookie의 id를 확인해서 redis에서 list는 가져온다.
            // TODO redis hash: k -> redisSession: cart: ->value , HK -> bookId, HV -> title/sellingPrice/quantity
            Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(cartSessionId);
            for (Map.Entry<Object, Object> entry : cartMap.entrySet()) {
                String[] values = entry.getValue().toString().split("/");
                cart.add(new CartResponseDto(Long.parseLong(entry.getKey().toString()),
                        values[0],
                        Long.parseLong(values[1]),
                        Integer.parseInt(values[2])));
            }
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<String> addToCart(@CookieValue(name = "cartSession", required = false) String cartSessionId,
                                            @RequestHeader(name = "X-Customer-Id", required = false, defaultValue = "0") long customerId,
                                            @RequestBody CartRequestDto cartRequestDto) {
        if (customerId == 0) {
            // 비회원 로직
            if (cartSessionId == null) {
                String newCartSessionId = "cart:" + UUID.randomUUID();
                redisTemplate.opsForHash().put(newCartSessionId, cartRequestDto.bookId(), cartRequestDto.quantity());
            } else {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(cartSessionId))
                        && !redisTemplate.opsForHash().hasKey(cartSessionId, cartRequestDto.bookId())) {
                    redisTemplate.opsForHash().put(cartSessionId, cartRequestDto.bookId(), cartRequestDto.title() + "/" + cartRequestDto.sellingPrice() + "/" + cartRequestDto.quantity());
                } else {
                    // TODO BAD_REQUEST
                }
            }
        } else {
            // 회원 로직
            cartService.addCart(cartRequestDto, customerId);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DELETE

    // UPDATE
}
