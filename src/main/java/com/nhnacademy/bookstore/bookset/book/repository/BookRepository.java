package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/**
 * 도서 Repository
 *
 * @author : 이유현
 * @date : 2024-10-29
 */
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

    Page<Book> findAll(Pageable pageable);

}
