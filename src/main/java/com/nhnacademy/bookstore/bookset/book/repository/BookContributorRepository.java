package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookContributorRepository extends JpaRepository<BookContributor, BookContributor.BookContributorId> {
    void deleteByBook(Book book);
}