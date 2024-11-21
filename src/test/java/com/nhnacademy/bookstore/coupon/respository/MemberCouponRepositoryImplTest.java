package com.nhnacademy.bookstore.coupon.respository;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.coupon.CouponRepository;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.repository.member.impl.MemberCouponRepositoryImpl;
import com.nhnacademy.bookstore.coupon.repository.policy.CouponPolicyRepository;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.nhnacademy.bookstore.user.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MemberCouponRepositoryImplTest
 *
 * 이 클래스는 MemberCouponRepositoryImpl의 getAllMemberCoupons 메서드를 테스트합니다.
 * 특정 회원의 쿠폰과 쿠폰 정책이 데이터베이스에 올바르게 저장되고 조회되는지 확인하는 테스트입니다.
 *
 * @since 1.0
 * author Luha
 */
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MemberCouponRepositoryImplTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepositoryImpl memberCouponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponJpaRepository;


    /**
     * getAllMemberCoupons 메서드 테스트.
     *
     * Given: 특정 회원의 쿠폰과 정책을 저장.
     * When: getAllMemberCoupons 메서드를 사용하여 쿠폰 목록을 조회.
     * Then: 조회된 쿠폰 목록이 올바른지 확인.
     */
    @Test
    @Transactional
    void testGetAllMemberCoupons() {
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(
                "TestUser",    // 이름
                "010-1234-5678", // 전화번호
                "testuser@example.com", // 이메일
                "testLoginId",  // 로그인 ID
                "password",      // 비밀번호
                "nickname",      // 닉네임
                Gender.M,     // 성별
                LocalDate.of(1990, 1, 1) // 생일
        );

        // 빈 Member 객체 생성 후 toEntity로 초기화
        Member member = new Member();
        member.toEntity(requestDto, "password");

        // 추가 필드 설정 (status, tier 등)
        member.setStatus(new MemberStatus(1L, "Active")); // 예시용 MemberStatus
        member.setTier(new MemberTier(1L, Tier.골드, true, 1, 1, 1)); // 예시용 MemberTier

        Member testMember = memberRepository.save(member);

        CouponPolicy policy = new CouponPolicy(null, "Test Policy", DiscountType.ACTUAL, 10, 100, 30, "Policy Detail", 50, false);
        couponPolicyRepository.save(policy);

        Coupon coupon = new Coupon(null, "Test Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
        Coupon coupon1 = couponRepository.save(coupon);
        MemberCoupon memberCoupon = new MemberCoupon(null, coupon1, testMember, LocalDateTime.now(), LocalDateTime.now().plusDays(5), false, null);
        memberCouponJpaRepository.save(memberCoupon);

        // When: getAllMemberCoupons 호출
        List<MemberCouponResponseDto> memberCoupons = memberCouponRepository.getAllMemberCoupons(testMember.getId());

        // Then: 조회된 데이터 검증
        assertNotNull(memberCoupons);
        assertEquals(1, memberCoupons.size());
        assertEquals("Test Coupon", memberCoupons.get(0).couponResponseDto().name());
        assertEquals("Test Policy", memberCoupons.get(0).couponResponseDto().couponPolicyResponseDto().name());
    }
}
