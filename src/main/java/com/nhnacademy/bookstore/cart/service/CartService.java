package com.nhnacademy.bookstore.cart.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.cart.dto.CartRequestDto;
import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.entity.CartId;
import com.nhnacademy.bookstore.cart.mapper.CartMapper;
import com.nhnacademy.bookstore.cart.repository.CartRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    private final CartMapper cartMapper;

    public List<CartResponseDto> getCartByCustomerId(long customerId) {
        return cartMapper.toCartResponseDtoList(cartRepository.findCartsByCustomerId(customerId));
    }

    public int addCart(CartRequestDto cartRequestDto, long customerId) {
        Book book = bookRepository.findById(cartRequestDto.bookId()).orElseThrow(null);
        Member member = memberRepository.findById(customerId).orElseThrow(null);

        if (cartRepository.existsById(new CartId(cartRequestDto.bookId(), customerId))) {
            return -1;
        } else {
            if (book.getRemainQuantity() - cartRequestDto.quantity() < 0) {
                return 0;
            }
            cartRepository.save(new Cart(book, member, cartRequestDto.quantity()));
            return 1;
        }
    }

    public void removeCart(CartId cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        }
    }

    public void updateCart(CartRequestDto cartRequestDto, long customerId) {
        if (cartRepository.existsById(new CartId(cartRequestDto.bookId(), customerId))) {
            cartRepository.updateQuantity(cartRequestDto.bookId(), cartRequestDto.quantity());
        }
    }
}
