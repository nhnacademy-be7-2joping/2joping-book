package com.nhnacademy.bookstore.like.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.dto.response.MemberLikeResponseDto;
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
import static org.mockito.Mockito.*;

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

    // 책이 존재하지 않을 때 좋아요 개수를 조회
    @Test
    void testGetLikeCount_BookNotFound() {
        Long bookId = 1L;

        given(bookRepository.existsById(bookId)).willReturn(false);

        assertThrows(NotFoundException.class, () -> likeService.getLikeCount(bookId));
        verify(bookRepository).existsById(bookId);
    }

    // 책이 존재하고 좋아요 개수를 반환
    @Test
    void testGetLikeCount_BookFound() {
        Long bookId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(true);
        given(likeRepository.getMemberLikesNum(bookId)).willReturn(5L);

        Long likeCount = likeService.getLikeCount(bookId);

        assertThat(likeCount).isEqualTo(5L);
        verify(likeRepository).getMemberLikesNum(bookId);
    }

    // 회원이 존재하지 않을 때 좋아요 목록 조회
    @Test
    void testGetBooksLikedByCustomer_MemberNotFound() {
        Long customerId = 1L;

        given(memberRepository.existsById(customerId)).willReturn(false);

        assertThrows(NotFoundException.class, () -> likeService.getBooksLikedByCustomer(customerId));
        verify(memberRepository).existsById(customerId);
    }

    // 회원이 존재하지만 좋아요 목록이 비어 있는 경우
    @Test
    void testGetBooksLikedByCustomer_NoLikes() {
        Long customerId = 1L;

        given(memberRepository.existsById(customerId)).willReturn(true);
        given(likeRepository.findLikesByMember(customerId)).willReturn(List.of());

        List<MemberLikeResponseDto> likes = likeService.getBooksLikedByCustomer(customerId);

        assertThat(likes).isEmpty();
        verify(likeRepository).findLikesByMember(customerId);
    }

    @Test
    void testGetBooksLikedByCustomer_WithLikes() {
        Long customerId = 1L;

        // Mock 동작 정의
        given(memberRepository.existsById(customerId)).willReturn(true);
        given(likeRepository.findLikesByMember(customerId)).willReturn(List.of(
                new MemberLikeResponseDto(1L, 1L,"url","Book 1"),
                new MemberLikeResponseDto(2L, 2L,"url","Book 2")
        ));

        // 테스트 실행
        List<MemberLikeResponseDto> likes = likeService.getBooksLikedByCustomer(customerId);

        // 검증
        assertThat(likes).hasSize(2);
        assertThat(likes.get(0).bookId()).isEqualTo(1L);
        assertThat(likes.get(0).title()).isEqualTo("Book 1");
        assertThat(likes.get(1).bookId()).isEqualTo(2L);
        assertThat(likes.get(1).title()).isEqualTo("Book 2");

        // 동작 검증
        verify(likeRepository).findLikesByMember(customerId);
    }


    // 회원이 존재하지 않을 때 좋아요 설정 시도
    @Test
    void testSetBookLike_MemberNotFound() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        LikeRequestDto requestDto = new LikeRequestDto(1L);

        assertThatThrownBy(() -> likeService.setBookLike(requestDto, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("존재하지 않은 회원입니다.");
    }

    // 책이 존재하지 않을 때 좋아요 설정 시도
    @Test
    void testSetBookLike_BookNotFound() {
        Long customerId = 1L;
        LikeRequestDto requestDto = new LikeRequestDto(1L);

        given(memberRepository.findById(customerId)).willReturn(Optional.of(new Member()));
        given(bookRepository.findById(requestDto.bookId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.setBookLike(requestDto, customerId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("책 정보가 없습니다.");
    }

    // 좋아요를 새롭게 추가
    @Test
    void testSetBookLike_AddNewLike() {
        Long customerId = 1L;
        Long bookId = 1L;
        LikeRequestDto requestDto = new LikeRequestDto(bookId);

        // Mock 객체 생성
        Member member = new Member();
        Book book = new Book();
        ReflectionTestUtils.setField(book, "bookId", bookId);

        // Mock 동작 정의
        given(memberRepository.findById(customerId)).willReturn(Optional.of(member));
        given(bookRepository.existsById(bookId)).willReturn(true); // 책 존재 여부
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(likeRepository.findBookLike(customerId, bookId)).willReturn(Optional.empty());
        given(likeRepository.getMemberLikesNum(bookId)).willReturn(1L); // 좋아요 개수

        // 새 좋아요 객체
        Like newLike = new Like(member, book);
        ReflectionTestUtils.setField(newLike, "likeId", 1L);
        given(likeRepository.save(any(Like.class))).willReturn(newLike);

        // 테스트 실행
        LikeResponseDto response = likeService.setBookLike(requestDto, customerId);

        // 검증
        assertThat(response.likeId()).isEqualTo(1L);
        assertThat(response.bookId()).isEqualTo(bookId);
        assertThat(response.likeCount()).isEqualTo(1L);

        verify(likeRepository).save(any(Like.class));
        verify(bookRepository).existsById(bookId); // 존재 여부 확인
        verify(bookRepository).findById(bookId);  // 책 조회 확인
    }

    @Test
    void testSetBookLike_RemoveExistingLike() {
        Long customerId = 1L;
        Long bookId = 1L;
        LikeRequestDto requestDto = new LikeRequestDto(bookId);

        // Mock 객체 생성
        Member member = new Member();
        Book book = new Book();
        Like existingLike = new Like(member, book);

        // 초기 값 설정
        ReflectionTestUtils.setField(member, "id", customerId); // Member ID 설정
        ReflectionTestUtils.setField(existingLike, "likeId", 1L); // 기존 좋아요 ID 설정
        ReflectionTestUtils.setField(book, "bookId", bookId); // Book ID 설정
        ReflectionTestUtils.setField(book, "likes", 5); // 초기 좋아요 수

        // Mock 동작 정의
        given(memberRepository.findById(customerId)).willReturn(Optional.of(member));
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(bookRepository.existsById(bookId)).willReturn(true); // 책 존재 여부 확인
        given(likeRepository.findBookLike(customerId, bookId)).willReturn(Optional.of(existingLike));
        doNothing().when(likeRepository).deleteById(existingLike.getLikeId()); // 기존 좋아요 삭제 Mock
        given(likeRepository.getMemberLikesNum(bookId)).willReturn(4L); // 삭제 후 좋아요 개수 Mock

        // 테스트 실행
        LikeResponseDto response = likeService.setBookLike(requestDto, customerId);

        // 검증
        assertThat(response.likeId()).isNull(); // 삭제된 경우 likeId는 null이어야 함
        assertThat(response.bookId()).isEqualTo(bookId); // bookId 확인
        assertThat(response.likeCount()).isEqualTo(4L); // 좋아요 개수 확인

        // 동작 검증
        verify(likeRepository).findBookLike(customerId, bookId); // 좋아요 조회 확인
        verify(likeRepository).deleteById(existingLike.getLikeId()); // 기존 좋아요 삭제 확인
        verify(bookRepository).save(book); // 좋아요 수 저장 확인
    }



}
