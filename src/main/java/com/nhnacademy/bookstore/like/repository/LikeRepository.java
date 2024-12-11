package com.nhnacademy.bookstore.like.repository;

import com.nhnacademy.bookstore.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeQuerydslRepository{
    @Query("SELECT bl FROM Like bl WHERE bl.book.bookId = :bookId AND bl.member.id = :customerId")
    Optional<Like> findBookLike(Long customerId, Long bookId);

    @Query("SELECT count(*) FROM Like bl WHERE bl.book.bookId = :bookId")
    Long getMemberLikesNum(Long bookId);

}
