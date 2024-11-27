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
