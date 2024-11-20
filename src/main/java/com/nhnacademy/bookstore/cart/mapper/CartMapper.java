package com.nhnacademy.bookstore.cart.mapper;

import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    List<CartResponseDto> toCartResponseDtoList (List<Cart> cartList);
}
