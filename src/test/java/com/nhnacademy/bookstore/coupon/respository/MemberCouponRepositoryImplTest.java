package com.nhnacademy.bookstore.coupon.respository;


import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.dto.response.OrderCouponResponse;
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
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberstatus.repository.MemberStatusRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import com.nhnacademy.bookstore.user.tier.repository.MemberTierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import com.nhnacademy.bookstore.user.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * MemberCouponRepositoryImplTest
 * 특정 회원의 쿠폰과 쿠폰 정책 조회 테스트 클래스입니다.
 * 쿠폰 생성, 저장 및 조회 메서드의 정상 동작을 검증합니다.
 *
 * @author Luha
 * @since 1.0
 */
 @DataJpaTest
 @Import(QuerydslConfig.class) // QueryDSL 설정 추가
 @ActiveProfiles("test") // 테스트 프로파일 활성화
 @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

     @Autowired
     private MemberTierRepository memberTierRepository;

     @Autowired
     private MemberStatusRepository memberStatusRepository;

    /**
     * 테스트 데이터 초기화
     * 쿠폰 정책, 쿠폰, 회원 정보를 데이터베이스에 저장합니다.
     */
     @BeforeEach
     void setUp() {
         // 1. MemberStatus 및 MemberTier 초기화
         MemberStatus status = new MemberStatus(1L, "Active");
         memberStatusRepository.save(status); // 반드시 저장

         MemberTier tier = new MemberTier(1L, Tier.GOLD, true, 1, 1, 1);
         memberTierRepository.save(tier); // 반드시 저장

         // 2. Member 생성 및 저장
         Member member = getMember();
         member.setStatus(status); // 저장된 MemberStatus 참조
         member.setTier(tier); // 저장된 MemberTier 참조
         memberRepository.save(member);

         // 3. CouponPolicy 및 Coupon 생성
         CouponPolicy policy = new CouponPolicy(null, "Test Policy", DiscountType.ACTUAL, 10, 100, 30, "Policy Detail", 50, false);
         couponPolicyRepository.save(policy);

         Coupon coupon = new Coupon(null, "Test Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
         couponRepository.save(coupon);

         Coupon usedCoupon = new Coupon(null, "Used Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
         couponRepository.save(usedCoupon);

         Coupon expiredCoupon = new Coupon(null, "Expired Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 100, policy, null);
         couponRepository.save(expiredCoupon);

         // 4. MemberCoupon 생성 및 저장
         MemberCoupon memberCoupon = new MemberCoupon(null, coupon, member, LocalDateTime.now(), LocalDateTime.now().plusDays(5), false, null);
         memberCouponJpaRepository.save(memberCoupon);

         MemberCoupon memberUsedCoupon = new MemberCoupon(null, usedCoupon, member, LocalDateTime.now(), LocalDateTime.now().plusDays(5), true, null);
         memberCouponJpaRepository.save(memberUsedCoupon);

         MemberCoupon memberExpiredCoupon = new MemberCoupon(null, expiredCoupon, member, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), true, null);
         memberCouponJpaRepository.save(memberExpiredCoupon);
     }

    /**
     * 특정 회원의 쿠폰 조회 테스트
     * Given: 회원과 쿠폰이 저장된 상태.
     * When: getAllMemberCoupons 호출.
     * Then: 저장된 쿠폰 목록을 반환.
     * @author Luha
     * @since 1.0
     */
    @Test
    @DisplayName("회원의 모든 쿠폰 조회 - 성공")
    void testGetAllMemberCoupons() {
         // Given
         Member testMember = memberRepository.findAll().getFirst();

         // When
         List<MemberCouponResponseDto> memberCoupons = memberCouponRepository.getAllMemberCoupons(testMember.getId());

         // Then
         assertThat(memberCoupons).isNotEmpty();
         assertThat(memberCoupons.getFirst().couponResponseDto().name()).isEqualTo("Test Coupon");
         assertThat(memberCoupons.getFirst().couponResponseDto().couponPolicyResponseDto().name()).isEqualTo("Test Policy");
     }

    /**
     * 특정 회원의 만료된 또는 사용된 쿠폰 조회 테스트
     * Given: 회원과 만료된 및 사용된 쿠폰이 저장된 상태.
     * When: getExpiredOrUsedMemberCoupons 호출.
     * Then: 만료되거나 사용된 쿠폰 목록을 반환.
     * @author Luha
     * @since 1.0
     */
    @Test
    @DisplayName("회원의 만료 또는 사용된 쿠폰 조회 - 성공")
    void testGetAllMemberOrderCoupons() {
         // Given
         Member testMember = memberRepository.findAll().getFirst();

         // When
         List<OrderCouponResponse> orderCoupons = memberCouponRepository.getAllMemberOrderCoupons(testMember.getId());

         // Then
         assertThat(orderCoupons).isNotEmpty();
         assertThat(orderCoupons.getFirst().name()).isEqualTo("Test Coupon");
         assertThat(orderCoupons.getFirst().detail()).isEqualTo("Policy Detail");
         assertThat(orderCoupons.getFirst().discountValue()).isEqualTo(10);
         assertThat(orderCoupons.getFirst().maxDiscount()).isEqualTo(50);
         assertThat(orderCoupons.getFirst().discountType()).isEqualTo(DiscountType.ACTUAL);
     }

     private static Member getMember() {
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

         Member member = new Member();
         member.toEntity(requestDto, "password");
         member.setStatus(new MemberStatus(1L, "Active"));
         member.setTier(new MemberTier(1L, Tier.GOLD, true, 1, 1, 1));
         return member;
     }

    /**
     * 특정 회원의 만료된 또는 사용된 쿠폰 조회 테스트
     * Given: 회원과 만료된 및 사용된 쿠폰이 저장된 상태.
     * When: getExpiredOrUsedMemberCoupons 호출.
     * Then: 만료되거나 사용된 쿠폰 목록을 반환.
     * @author Luha
     * @since 1.0
     */
    @Test
    @DisplayName("회원의 만료 또는 사용된 쿠폰 조회 - 성공")
    void testGetExpiredOrUsedMemberCoupons() {
         // Given
         Member member = memberRepository.findAll().getFirst();

         // When
         List<MemberCouponResponseDto> result = memberCouponRepository.getExpiredOrUsedMemberCoupons(member.getId());

         // Then
         assertThat(result).hasSize(2);

         MemberCouponResponseDto expiredCoupon = null;
         MemberCouponResponseDto usedCoupon = null;

         for (MemberCouponResponseDto dto : result) {
             if ("Expired Coupon".equals(dto.couponResponseDto().name())) {
                 expiredCoupon = dto;
             } else if ("Used Coupon".equals(dto.couponResponseDto().name())) {
                 usedCoupon = dto;
             }

             // 두 쿠폰을 모두 찾으면 루프 종료
             if (expiredCoupon != null && usedCoupon != null) {
                 break;
             }
         }
         assertThat(expiredCoupon).isNotNull();
         assertThat(expiredCoupon.invalidTime()).isBefore(LocalDateTime.now());

         assertThat(usedCoupon).isNotNull();
         assertThat(usedCoupon.isUsed()).isTrue();
     }
 }