package com.nhnacademy.bookstore.cart.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.cart.dto.CartRequestDto;
import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.entity.CartId;
import com.nhnacademy.bookstore.cart.mapper.CartMapper;
import com.nhnacademy.bookstore.cart.repository.CartRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("회원의 장바구니 조회 성공 테스트")
    void getCartByCustomerId() {
        // given
        long customerId = 1L;

        Publisher publisher = mock(Publisher.class);

        Book book1 = new Book(
                1L,
                publisher,
                "책1",
                "Description1",
                LocalDate.now(),
                "ISBN1234567890",
                10000,
                9000,
                true,
                true,
                10, // remainQuantity
                0,  // views
                0,  // likes
                null // likedBy
        );

        Book book2 = new Book(
                2L,
                publisher,
                "책2",
                "Description2",
                LocalDate.now(),
                "ISBN0987654321",
                15000,
                14000,
                true,
                true,
                5, // remainQuantity
                0, // views
                0, // likes
                null
        );

        Member member = mock(Member.class);
        when(member.getId()).thenReturn(customerId);

        Cart cart1 = new Cart(book1, member, 2);
        Cart cart2 = new Cart(book2, member, 3);
        List<Cart> carts = List.of(cart1, cart2);

        List<CartResponseDto> responseDtos = List.of(
                new CartResponseDto(1L, "책1", 9000, 2),
                new CartResponseDto(2L, "책2", 14000, 3)
        );

        when(cartRepository.findCartsByCustomerId(customerId)).thenReturn(carts);
        when(cartMapper.toCartResponseDtoList(carts)).thenReturn(responseDtos);

        // when
        List<CartResponseDto> result = cartService.getCartByCustomerId(customerId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("책1", result.get(0).title());
        assertEquals("책2", result.get(1).title());
    }

    @Test
    @DisplayName("장바구니 상품 추가 성공 테스트")
    void addCartSuccess() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 9000, 2);

        Publisher publisher = mock(Publisher.class);

        Book book = new Book(
                1L,
                publisher,
                "책1",
                "Description",
                LocalDate.now(),
                "ISBN1234567890",
                10000,
                9000,
                true,
                true,
                5, // remainQuantity
                0,  // views
                0,  // likes
                null // likedBy
        );

        Member member = mock(Member.class);
        when(member.getId()).thenReturn(customerId);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(cartRepository.existsById(new CartId(1L, customerId))).thenReturn(false);

        // when
        int result = cartService.addCart(requestDto, customerId);

        // then
        assertEquals(1, result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 상품 추가 실패 테스트 - 도서 없음")
    void addCartBookNotFound() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 9000, 2);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NullPointerException.class, () -> cartService.addCart(requestDto, customerId));
    }

    @Test
    @DisplayName("장바구니 상품 추가 실패 테스트 - 회원 없음")
    void addCartMemberNotFound() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 9000, 2);

        Publisher publisher = mock(Publisher.class);

        Book book = new Book(
                1L,
                publisher,
                "책1",
                "Description",
                LocalDate.now(),
                "ISBN1234567890",
                10000,
                9000,
                true,
                true,
                5, // remainQuantity
                0,  // views
                0,  // likes
                null // likedBy
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NullPointerException.class, () -> cartService.addCart(requestDto, customerId));
    }

    @Test
    @DisplayName("장바구니 추가 실패 테스트 - 이미 존재하는 상품")
    void addCart_ItemAlreadyExists_ReturnsMinusOne() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 1);
        Book book = mock(Book.class);
        Member member = mock(Member.class);

        // mock 설정
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(cartRepository.existsById(new CartId(requestDto.bookId(), customerId))).thenReturn(true);

        // when
        int result = cartService.addCart(requestDto, customerId);

        // then
        assertEquals(-1, result);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 추가 실패 테스트 - 재고 부족")
    void addCart_InsufficientStock_ReturnsZero() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 5);
        Book book = mock(Book.class);
        Member member = mock(Member.class);

        // mock 설정
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(cartRepository.existsById(new CartId(requestDto.bookId(), customerId))).thenReturn(false);
        when(book.getRemainQuantity()).thenReturn(3); // 재고 부족

        // when
        int result = cartService.addCart(requestDto, customerId);

        // then
        assertEquals(0, result);
        verify(cartRepository, never()).save(any(Cart.class));
    }



    @Test
    @DisplayName("장바구니 상품 삭제 성공 테스트")
    void removeCartSuccess() {
        // given
        long customerId = 1L;
        CartId cartId = new CartId(1L, customerId);

        when(cartRepository.existsById(cartId)).thenReturn(true);

        // when
        cartService.removeCart(cartId);

        // then
        verify(cartRepository).deleteById(cartId);
    }

    @Test
    @DisplayName("장바구니 상품 삭제 실패 테스트 - 장바구니 없음")
    void removeCartNotFound() {
        // given
        long customerId = 1L;
        CartId cartId = new CartId(1L, customerId);

        when(cartRepository.existsById(cartId)).thenReturn(false);

        // when
        cartService.removeCart(cartId);

        // then
        verify(cartRepository, never()).deleteById(any(CartId.class));
    }

    @Test
    @DisplayName("장바구니 상품 수정 성공 테스트")
    void updateCartSuccess() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 9000, 5);
        CartId cartId = new CartId(1L, customerId);

        when(cartRepository.existsById(cartId)).thenReturn(true);

        // when
        cartService.updateCart(requestDto, customerId);

        // then
        verify(cartRepository).updateQuantity(1L, 5);
    }

    @Test
    @DisplayName("장바구니 상품 수정 실패 테스트 - 장바구니 없음")
    void updateCartNotFound() {
        // given
        long customerId = 1L;
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 9000, 5);
        CartId cartId = new CartId(1L, customerId);

        when(cartRepository.existsById(cartId)).thenReturn(false);

        // when
        cartService.updateCart(requestDto, customerId);

        // then
        verify(cartRepository, never()).updateQuantity(anyLong(), anyInt());
    }

    @Test
    @DisplayName("회원의 빈 장바구니 조회 테스트")
    void getCartByCustomerIdEmpty() {
        // given
        long customerId = 1L;
        List<Cart> carts = List.of();

        when(cartRepository.findCartsByCustomerId(customerId)).thenReturn(carts);
        when(cartMapper.toCartResponseDtoList(carts)).thenReturn(List.of());

        // when
        List<CartResponseDto> result = cartService.getCartByCustomerId(customerId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
