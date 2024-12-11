package com.nhnacademy.bookstore.like.repository;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.like.dto.response.MemberLikeResponseDto;
import com.nhnacademy.bookstore.like.entity.Like;
import com.nhnacademy.bookstore.like.repository.impl.LikeQuerydslRepositoryImpl;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 및 설정 클래스 포함
@ActiveProfiles("test") // test 프로파일 활성화
@ImportAutoConfiguration(exclude = MySqlConfig.class) // MySqlConfig 비활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikeRepositoryTest {

    @Autowired
    private LikeQuerydslRepositoryImpl likeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private Book book;
    private Image image;

    @BeforeEach
    void setUp() {
        // MemberStatus와 MemberTier 생성 및 저장
        MemberStatus status = entityManager.find(MemberStatus.class, 1L);
        if (status == null) {
            status = new MemberStatus(1L, "가입");
            entityManager.merge(status); // 새로 생성된 경우 persist 호출
        }

        MemberTier tier = entityManager.find(MemberTier.class, 1L);
        if (tier == null) {
            tier = new MemberTier(1L, Tier.NORMAL, true, 1, 1, 1);
            entityManager.merge(tier); // 새로 생성된 경우 persist 호출
        }

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
        entityManager.persist(member);

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

        // Image와 BookImage 생성 및 저장
        Image image = new Image(null, "thumbnail.jpg");
        entityManager.persist(image);

        BookImage bookImage = new BookImage(book, image, "썸네일");
        entityManager.persist(bookImage);

        // Like 생성 및 저장
        Like like = new Like(member, book);
        entityManager.persist(like);

        // 강제로 flush 및 clear 호출
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    void testFindLikesByMember() {
        // When
        List<MemberLikeResponseDto> likes = likeRepository.findLikesByMember(member.getId());

        // Then
        assertThat(likes).hasSize(1);
        MemberLikeResponseDto likeResponse = likes.get(0);
        assertThat(likeResponse.likeId()).isNotNull();
        assertThat(likeResponse.bookId()).isEqualTo(book.getBookId());
        assertThat(likeResponse.url()).isEqualTo("thumbnail.jpg");
        assertThat(likeResponse.title()).isEqualTo("Test Book");
    }
}
