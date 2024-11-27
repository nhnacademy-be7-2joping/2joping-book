package com.nhnacademy.bookstore.imageset.repository;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    List<BookImage> findByBookAndImageType(Book book, String imageType);
    boolean existsByImage(Image image);
}
