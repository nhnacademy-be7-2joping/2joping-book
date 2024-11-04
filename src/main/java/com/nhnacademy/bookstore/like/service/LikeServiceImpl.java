package com.nhnacademy.bookstore.like.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.entity.Like;
import com.nhnacademy.bookstore.like.repository.LikeRepository;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
    public LikeResponseDto setBookLike(LikeRequestDto request) {

        // 회원 검증
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException("존재하지 않은 회원입니다."));

        // 책 검증
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("책 정보가 없습니다."));

        // 기존 좋아요 확인
        Optional<Like> optionalBookLike =

                likeRepository.findBookLike(member.getLoginId(), book.getBookId());
        // login id 보다는 customer id 받아오는게 맞는 것 같은데 customer에는 getter가 없어서 어떻게 받아오는지 모르겠음
        // 그리구 아직 customer 구현이 안됨
        // entity 에서 long->string으로 수정한 상태 (필요하면 다시 고치기)

        // 동일한 좋아요가 없으면 생성
        if (optionalBookLike.isEmpty()) {
            Like bookLike = new Like(null, member, book); // 생성자 사용
            // 근데 likeid가 왜 null?

            Like savedBookLike = likeRepository.save(bookLike);

            return new LikeResponseDto(
                    savedBookLike.getLikeId(),
                    savedBookLike.getBook().getBookId(),
                    savedBookLike.getMember().getId(), // customer의 ID 반환 / 일단 오류 없애려고 dto memberid값을 string으로 수정해둠
                    // 마찬가지로 getter없음... //아오 이거 해결하고 swagger 돌려보고 testcode 짜던지말던지!!!!!!!!!!!!!!!!
                    getLikeCount(book.getBookId()) // 책의 좋아요 수
            );
        } else {
            // 좋아요가 존재하면 삭제
            Like bookLike = optionalBookLike.get();
            likeRepository.deleteById(bookLike.getLikeId()); // 인스턴스 메서드 호출

            return new LikeResponseDto(
                    null,
                    request.bookId(),
                    request.memberId(),
                    getLikeCount(book.getBookId()) // 책의 좋아요 수
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
        // 특정 책의 좋아요 수 조회
        return likeRepository.getMemberLikesNum(bookId);
    }

    /**
     * 특정 사용자가 좋아요를 누른 책 목록을 조회하는 메서드
     *
     * @param customerId 사용자 ID
     * @return List<Book> 좋아요를 누른 책 목록
     */
    @Override
    public List<Book> getBooksLikedByCustomer(Long customerId) {
        // 특정 사용자가 좋아요를 누른 책 목록 조회
        return likeRepository.findBooksLikedByMember(customerId);
    }
}
