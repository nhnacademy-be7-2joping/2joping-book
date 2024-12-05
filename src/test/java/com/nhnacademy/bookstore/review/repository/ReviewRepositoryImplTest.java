package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 및 설정 클래스 포함
@ActiveProfiles("test") // test 프로파일 활성화
@ImportAutoConfiguration(exclude = MySqlConfig.class) // MySqlConfig 비활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewRepositoryImplTest {

    @Autowired
    private ReviewRepositoryImpl reviewRepository;

    @Autowired
    private EntityManager entityManager;

    private Member member;
    private Book book;
    private OrderDetail orderDetail;
    private Review review;

    @BeforeEach
    void setUp() {
        // MemberStatus와 MemberTier 생성 및 저장
        MemberStatus status = new MemberStatus(1L, "가입");
        entityManager.merge(status);

        MemberTier tier = new MemberTier(1L, Tier.NORMAL, true, 1, 1, 1);
        entityManager.merge(tier);

        // Member 생성 및 저장
        member = new Member(
                "testLoginId",
                "testPassword",
                "nickname",
                Gender.M,
                LocalDate.of(1990, 1, 1),
                1,
                LocalDate.now(),
                LocalDate.now(),
                false,
                0,
                0,
                List.of(),
                null,
                status,
                tier,
                null
        );
        member.initializeCustomerFields("이름", "010-1111-1111", "email@naver.com");
        entityManager.persist(member); // persist 사용

        // Publisher와 Book 생성 및 저장
        Publisher publisher = new Publisher("출판사 이름");
        entityManager.persist(publisher);

        book = new Book(
                null,
                publisher,
                "Test Book",
                "This is a test book.",
                LocalDate.now(),
                "1234567890123",
                10000,
                9000,
                true,
                true,
                10,
                0,
                0,
                null
        );
        entityManager.persist(book);

        OrderState orderState = new OrderState(OrderStateType.WAITING);
        orderState.setOrderStateId(1L);
        entityManager.persist(orderState);

        // Order 생성 및 저장
        Order order = new Order(
                null,
                "ORD123",
                orderState,
                member, // 영속화된 Member 설정
                null,
                LocalDateTime.now(),
                null,
                "Test Receiver",
                "12345",
                "Test Address",
                "Detail Address",
                0,
                2500,
                0,
                11000,
                null
        );
        entityManager.persist(order);

        // OrderDetail 생성 및 저장
        orderDetail = new OrderDetail(
                null,
                order,
                book,
                1,
                9000,
                9000
        );
        entityManager.persist(orderDetail);

        // Review 생성 및 저장
        review = new Review(
                null,
                orderDetail,
                member,
                book,
                "Great Book!",
                "Loved reading this book.",
                5,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                null
        );
        entityManager.persist(review);

        // 강제로 flush 및 clear 호출
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    void testGetReviewsByBookId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseDto> reviews = reviewRepository.getReviewsByBookId(pageable, book.getBookId());

        assertThat(reviews.getTotalElements()).isEqualTo(1);
        assertThat(reviews.getContent().get(0).reviewId()).isEqualTo(review.getReviewId());
        assertThat(reviews.getContent().get(0).title()).isEqualTo("Great Book!");
    }

    @Test
    void testGetReviewsByCustomerId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewTotalResponseDto> reviews = reviewRepository.getReviewsByCustomerId(pageable, member.getId());

        assertThat(reviews.getTotalElements()).isEqualTo(1);
        assertThat(reviews.getContent().get(0).customerId()).isEqualTo(member.getId());
        assertThat(reviews.getContent().get(0).bookName()).isEqualTo("Test Book");
    }

    @Test
    void testGetReviewByReviewId() {
        Optional<ReviewResponseDto> reviewDto = reviewRepository.getReviewByReviewId(review.getReviewId());

        assertThat(reviewDto).isPresent();
        assertThat(reviewDto.get().reviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewDto.get().title()).isEqualTo("Great Book!");
    }
}

