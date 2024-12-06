package com.nhnacademy.bookstore.bookset.publisher.repository;

import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 및 설정 클래스 포함
@ActiveProfiles("test") // test 프로파일 활성화
@ImportAutoConfiguration(exclude = MySqlConfig.class) // MySqlConfig 비활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PublisherRepositoryImplTest {

    @Autowired
    private PublisherRepositoryImpl publisherRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 테스트용 Publisher 데이터 생성 및 저장
        for (long i = 1; i <= 15; i++) {
            Publisher publisher = new Publisher("Publisher " + i);
            entityManager.persist(publisher);
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindAllBy() {
        // Pageable 생성
        Pageable pageable = PageRequest.of(0, 10);

        // 메서드 호출
        Page<PublisherResponseDto> resultPage = publisherRepository.findAllBy(pageable);

        // 검증
        assertThat(resultPage.getTotalElements()).isEqualTo(15); // 총 Publisher 수
        assertThat(resultPage.getTotalPages()).isEqualTo(2); // 총 페이지 수
        assertThat(resultPage.getContent().size()).isEqualTo(10); // 현재 페이지 데이터 개수
        assertThat(resultPage.getContent().get(0).name()).isEqualTo("Publisher 1"); // 첫 번째 데이터 확인

        // 다음 페이지 조회 테스트
        Pageable nextPageable = PageRequest.of(1, 10);
        Page<PublisherResponseDto> nextPage = publisherRepository.findAllBy(nextPageable);

        assertThat(nextPage.getContent().size()).isEqualTo(5); // 마지막 페이지 데이터 개수
        assertThat(nextPage.getContent().get(0).name()).isEqualTo("Publisher 11"); // 11번째 데이터 확인
    }
}

