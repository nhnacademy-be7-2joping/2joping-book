package com.nhnacademy.bookstore.review.mapper;


import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "reviewId", target = "reviewId")
    @Mapping(source = "ratingValue", target = "ratingValue")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "imageUrl", target = "reviewImage")
    @Mapping(source = "createdAt", target = "createdAt")
    ReviewCreateResponseDto toCreateResponseDto(Review review);

    @Mapping(source = "reviewId", target = "reviewId")
    @Mapping(source = "ratingValue", target = "ratingValue")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ReviewModifyResponseDto toModifyResponseDto(Review review);

    @Mapping(source = "reviewId", target = "reviewId")
    @Mapping(source = "orderDetail.orderDetailId", target = "orderDetailId")
    @Mapping(source = "member.id", target = "customerId")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "ratingValue", target = "ratingValue")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ReviewResponseDto toResponseDto(Review review);
}
