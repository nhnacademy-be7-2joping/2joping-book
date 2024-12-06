package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 도서 Repository
 *
 * @author : 이유현
 * @date : 2024-10-29
 */
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findByBookIdIn(List<Long> ids);

    List<Book> findByBookIdIn(Set<Long> ids);

    @Query("SELECT b.remainQuantity FROM Book b WHERE b.bookId = :bookId")
    Optional<Integer> findRemainQuantityByBookId(@Param("bookId") Long bookId);
}
