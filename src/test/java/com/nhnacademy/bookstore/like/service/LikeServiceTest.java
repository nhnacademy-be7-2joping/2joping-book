package com.nhnacademy.bookstore.like.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.entity.Like;
import com.nhnacademy.bookstore.like.repository.LikeRepository;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testSetBookLike_CreateLike() {
        // 회원 객체 생성
        Member member = new Member(
                "user1", "password", "nickname", Gender.M,
                LocalDate.of(2003, 4, 25), 1, LocalDate.now(), LocalDate.now(), false,
                0, 0, List.of(), new MemberStatus(), new MemberTier(), null
        );
        ReflectionTestUtils.setField(member, "id", 1L);

        // 책 객체 생성
        Book book = new Book(
                1L, null, "Sample Book", "A description", LocalDate.of(2020, 1, 1),
                "1234567890123", 1000, 900, true, true, 10, 100, 50
        );
        ReflectionTestUtils.setField(book, "bookId", 1L);

        // Mocking behavior 설정
        given(bookRepository.existsById(1L)).willReturn(true);
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(likeRepository.findBookLike(1L, 1L)).willReturn(Optional.empty());

        Like like = new Like(member, book);
        given(likeRepository.save(any(Like.class))).willReturn(like);

        LikeRequestDto requestDto = new LikeRequestDto(1L, 1L);
        LikeResponseDto responseDto = likeService.setBookLike(requestDto);

        assertThat(responseDto).isNotNull();

        verify(likeRepository).save(any(Like.class));
    }


    @Test
    void testSetBookLike_DeleteLike() {
        Member member = new Member(
                "user1", "password", "nickname", Gender.F,
                LocalDate.of(1995, 5, 5), 1, LocalDate.now(), LocalDate.now(), false,
                0, 0, List.of(), new MemberStatus(), new MemberTier(), null
        );
        ReflectionTestUtils.setField(member, "id", 1L);

        Book book = new Book(
                1L, null, "Sample Book", "A description", LocalDate.of(2020, 1, 1),
                "1234567890123", 1000, 900, true, true, 10, 100, 50
        );
        ReflectionTestUtils.setField(book, "bookId", 1L);


        Like existingLike = new Like(member, book);
        ReflectionTestUtils.setField(existingLike, "likeId", 1L);


        given(bookRepository.existsById(1L)).willReturn(true);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(likeRepository.findBookLike(1L, 1L)).willReturn(Optional.of(existingLike));

        LikeRequestDto requestDto = new LikeRequestDto(1L, 1L);
        LikeResponseDto responseDto = likeService.setBookLike(requestDto);

        assertThat(responseDto).isNotNull();

        verify(likeRepository).deleteById(existingLike.getLikeId());
    }


    @Test
    void testGetBooksLikedByCustomer() {
        Member member = new Member(
                "user1", "password", "nickname", Gender.F,
                LocalDate.of(1995, 5, 5), 1, LocalDate.now(), LocalDate.now(), false,
                0, 0, List.of(), new MemberStatus(), new MemberTier(), null
        );
        ReflectionTestUtils.setField(member, "id", 1L);

        Book book1 = new Book(
                1L, null, "Sample Book 1", "A description", LocalDate.of(2020, 1, 1),
                "1234567890123", 1000, 900, true, true, 10, 100, 50
        );
        ReflectionTestUtils.setField(book1, "bookId", 1L);

        Book book2 = new Book(
                2L, null, "Sample Book 2", "Another description", LocalDate.of(2021, 2, 2),
                "1234567890456", 1200, 1100, true, true, 15, 120, 60
        );
        ReflectionTestUtils.setField(book2, "bookId", 2L);

        given(memberRepository.existsById(1L)).willReturn(true);
        given(likeRepository.findBooksLikedByMember(1L)).willReturn(List.of(book1, book2));

        List<Book> likedBooks = likeService.getBooksLikedByCustomer(1L);

        assertThat(likedBooks).containsExactly(book1, book2);

        verify(memberRepository).existsById(1L);
        verify(likeRepository).findBooksLikedByMember(1L);
    }

    @Test
    void testGetLikeCount_BookNotFound() {
        Long bookId = 1L;

        given(bookRepository.existsById(bookId)).willReturn(false);

        assertThrows(NotFoundException.class, () -> {
            likeService.getLikeCount(bookId);
        });


    }

    @Test
    void testGetBooksLikedByCustomer_MemberNotFound() {
        Long customerId = 1L;

        given(memberRepository.existsById(customerId)).willReturn(false);

        assertThrows(NotFoundException.class, () -> {
            likeService.getBooksLikedByCustomer(customerId);
        });
    }



    @Test
    void testSetBookLike_MemberNotFound() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        LikeRequestDto requestDto = new LikeRequestDto(1L, 1L);

        assertThatThrownBy(() -> likeService.setBookLike(requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("존재하지 않은 회원입니다.");
    }


}
