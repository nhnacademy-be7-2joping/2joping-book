package com.nhnacademy.bookstore.review.mapper;


import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "reviewId", target = "orderDetailId")
    @Mapping(source = "ratingValue", target = "ratingValue")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "imageUrl", target = "image")
    @Mapping(source = "createdAt", target = "createdAt")
    ReviewCreateResponseDto toDto(Review review);

    // ReviewId를 Long으로 변환하는 헬퍼 메서드 추가
    default Long map(Review.ReviewId reviewId) {
        return reviewId != null ? reviewId.getOrderDetailId() : null;
    }

//    default Long map(ReviewPK reviewPK) {
//        return reviewPK != null ? reviewPK.getOrderDetailId() : null;
//    }
//
//    default ReviewPK map(Long orderDetailId) {
//        return orderDetailId != null ? new ReviewPK(orderDetailId) : null;
//    }
}
