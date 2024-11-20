package com.nhnacademy.bookstore.cart.service;

import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.entity.CartId;
import com.nhnacademy.bookstore.cart.mapper.CartMapper;
import com.nhnacademy.bookstore.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    public List<CartResponseDto> getCartByCustomerId(long customerId) {
        return cartMapper.toCartResponseDtoList(cartRepository.findCartsByCustomerId(customerId));
    }

    public boolean addCart(Cart cart) {
        if (cartRepository.existsById(new CartId(cart.getId().getBookId(), cart.getId().getCustomerId()))) {
            return false;
        }
        cartRepository.save(cart);
        return true;
    }

    public boolean removeCart(CartId cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return true;
        }
        return false;
    }

    public boolean updateCart(Cart cart) {
        if (cartRepository.existsById(new CartId(cart.getId().getBookId(), cart.getId().getCustomerId()))) {
            cartRepository.save(cart);
            return true;
        }
        return false;
    }
}
