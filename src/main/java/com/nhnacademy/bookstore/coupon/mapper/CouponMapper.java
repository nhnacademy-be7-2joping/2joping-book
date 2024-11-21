package com.nhnacademy.bookstore.coupon.mapper;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CouponMapper
 * 이 매퍼 인터페이스는 Coupon과 CouponPolicy 엔티티를 각각 CouponResponseDto와
 * CouponPolicyResponseDto로 변환하는 기능을 제공합니다. MapStruct를 사용하여 매핑을 자동화하며,
 * 스프링 컴포넌트로 등록되어 의존성 주입을 통해 사용됩니다.
 *
 * @author Luha
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface CouponMapper {

    /**
     * Coupon 엔티티를 CouponResponseDto로 매핑합니다.
     * couponPolicy 필드는 couponPolicyResponseDto로 변환됩니다.
     *
     * @return CouponResponseDto로 변환된 객체
     */
    @Mapping(source = "couponPolicy", target = "couponPolicyResponseDto")
    CouponResponseDto toCouponResponseDto(Coupon coupon);


    /**
     * CouponPolicy 엔티티를 CouponPolicyResponseDto로 매핑합니다.
     *
     * @return CouponPolicyResponseDto로 변환된 객체
     */
    CouponPolicyResponseDto toResponseDto(CouponPolicy couponPolicy);
}
