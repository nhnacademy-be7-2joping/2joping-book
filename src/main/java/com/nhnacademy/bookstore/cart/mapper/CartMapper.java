package com.nhnacademy.bookstore.cart.mapper;

import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "cart.book.bookId", target = "bookId")
    @Mapping(source = "cart.book.title", target = "title")
    @Mapping(source = "cart.book.sellingPrice", target = "sellingPrice")
    @Mapping(source = "cart.quantity", target = "quantity")
    CartResponseDto toCartResponseDto(Cart cart);

    List<CartResponseDto> toCartResponseDtoList(List<Cart> cartList);
}
