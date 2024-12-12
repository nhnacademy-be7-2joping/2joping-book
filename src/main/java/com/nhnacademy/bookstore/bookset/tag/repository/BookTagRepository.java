package com.nhnacademy.bookstore.bookset.tag.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.tag.entity.BookTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTagRepository extends JpaRepository<BookTag, BookTag.BookTagId> {
    boolean existsByBook_BookIdAndTag_TagId(Long bookId, Long tagId);

    void deleteByBook(Book book);
}
