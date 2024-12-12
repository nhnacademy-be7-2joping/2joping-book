package com.nhnacademy.bookstore.orderset.orderdetail.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = MySqlConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderDetailRepositoryImplTest {

    @Autowired
    private OrderDetailRepositoryImpl orderDetailRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private Order order;
    private Book book;
    private OrderDetail orderDetail;

    @BeforeEach
    void setUp() {
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

        // Book 생성 및 저장
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
        order = new Order(
                null,
                "ORD123",
                orderState,
                member,
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

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByOrderId() {
        // 메서드 호출
        List<OrderDetailResponseDto> result = orderDetailRepository.findByOrderId(order.getOrderId());

        // 검증
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).orderDetailId()).isEqualTo(orderDetail.getOrderDetailId());
        assertThat(result.get(0).bookTitle()).isEqualTo("Test Book");
    }

    @Test
    void testFindByCustomerId() {
        Pageable pageable = PageRequest.of(0, 10);

        // 메서드 호출
        Page<OrderDetailResponseDto> resultPage = orderDetailRepository.findByCustomerId(pageable, member.getId());

        // 검증
        assertThat(resultPage.getTotalElements()).isEqualTo(1); // 총 OrderDetail 수
        assertThat(resultPage.getContent().size()).isEqualTo(1); // 현재 페이지 데이터 개수
        assertThat(resultPage.getContent().get(0).bookTitle()).isEqualTo("Test Book"); // Book 제목 확인
        assertThat(resultPage.getContent().get(0).orderDate()).isNotNull(); // 주문 날짜 확인
    }
}
