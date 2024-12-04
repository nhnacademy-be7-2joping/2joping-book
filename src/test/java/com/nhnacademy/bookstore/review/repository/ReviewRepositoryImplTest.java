package com.nhnacademy.bookstore.review.repository;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.entity.ReviewImage;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.user.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
public class ReviewRepositoryImplTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("특정 도서 ID로 리뷰 조회 - 페이징 포함")
    void getReviewsByBookId() {
        // Given: 테스트 데이터 생성
        Book book = entityManager.persist(new Book(/* book 데이터 초기화 */));
        Review review1 = entityManager.persist(new Review(/* review1 데이터 초기화 */));
        Review review2 = entityManager.persist(new Review(/* review2 데이터 초기화 */));

        Pageable pageable = PageRequest.of(0, 10);

        // When: getReviewsByBookId 호출
        Page<ReviewResponseDto> result = reviewRepository.getReviewsByBookId(pageable, book.getBookId());

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(book.getBookId(), result.getContent().get(0).bookId());
    }

    @Test
    @DisplayName("특정 회원 ID로 리뷰 조회 - 페이징 포함")
    void getReviewsByCustomerId() {
        // Given: 테스트 데이터 생성
        Member member = entityManager.persist(new Member(/* member 데이터 초기화 */));
        Book book = entityManager.persist(new Book(/* book 데이터 초기화 */));
        Review review1 = entityManager.persist(new Review(/* review1 데이터 초기화 */));
        Review review2 = entityManager.persist(new Review(/* review2 데이터 초기화 */));

        Pageable pageable = PageRequest.of(0, 10);

        // When: getReviewsByCustomerId 호출
        Page<ReviewTotalResponseDto> result = reviewRepository.getReviewsByCustomerId(pageable, member.getId());

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(member.getId(), result.getContent().get(0).customerId());
    }

    @Test
    @DisplayName("특정 리뷰 ID로 리뷰 상세 조회")
    void getReviewByReviewId() {
        // Given: 테스트 데이터 생성
        Review review = entityManager.persist(new Review(/* review 데이터 초기화 */));
        Image image = entityManager.persist(new Image(/* image 데이터 초기화 */));
        ReviewImage reviewImage = entityManager.persist(new ReviewImage(review, image));

        // When: getReviewByReviewId 호출
        Optional<ReviewResponseDto> result = reviewRepository.getReviewByReviewId(review.getReviewId());

        // Then: 결과 검증
        assertTrue(result.isPresent());
        assertEquals(review.getReviewId(), result.get().reviewId());
        assertEquals(image.getUrl(), result.get().reviewImage());
    }
}
