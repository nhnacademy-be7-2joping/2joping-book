package com.nhnacademy.bookstore.like.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.dto.response.MemberLikeResponseDto;
import com.nhnacademy.bookstore.like.entity.Like;
import com.nhnacademy.bookstore.like.repository.LikeRepository;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * tag service
 *
 * @author : 박채연
 * @date : 2024-11-01
 */

@Service
@Slf4j
@RequiredArgsConstructor

public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    /**
     * 책에 좋아요를 설정하는 메서드
     *
     * @param request 책과 회원 정보가 아이디가 담긴 객체
     * @return LikeResponseDto
     */
    public LikeResponseDto setBookLike(LikeRequestDto request,Long customerId) {
        log.debug("1");
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("존재하지 않은 회원입니다."));
        log.debug("2");
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("책 정보가 없습니다."));
        log.debug("좋아요 도서 get");
        Optional<Like> optionalBookLike =

                likeRepository.findBookLike(member.getId(), book.getBookId());
        log.debug("IF문 전");
        if (optionalBookLike.isEmpty()) {
            log.debug("좋아요 새롭게 추가");
            Like bookLike = new Like(member, book);

            Like savedBookLike = likeRepository.save(bookLike);


            return new LikeResponseDto(
                    savedBookLike.getLikeId(),
                    savedBookLike.getBook().getBookId(),
                    savedBookLike.getMember().getId(),
                    getLikeCount(book.getBookId())
            );
        } else {
            log.debug("좋아요 삭제");
            Like bookLike = optionalBookLike.get();
            likeRepository.deleteById(bookLike.getLikeId());

            return new LikeResponseDto(
                    null,
                    request.bookId(),
                    customerId,
                    getLikeCount(book.getBookId())
            );
        }
    }

    /**
     * 특정 책의 좋아요 개수를 조회하는 메서드
     *
     * @param bookId 책 ID
     * @return Long 좋아요 개수
     */
    @Override
    public Long getLikeCount(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("존재하지 않는 책입니다.");
        }

        return likeRepository.getMemberLikesNum(bookId);
    }


    /**
     * 특정 사용자가 좋아요를 누른 책 목록을 조회하는 메서드
     *
     * @param customerId 사용자 ID
     * @return List<Book> 좋아요를 누른 책 목록
     */
    @Override
    public List<MemberLikeResponseDto> getBooksLikedByCustomer(Long customerId) {
        if (!memberRepository.existsById(customerId)) {
            throw new NotFoundException("존재하지 않는 회원입니다.");
        }
        return likeRepository.findLikesByMember(customerId);
    }
}
