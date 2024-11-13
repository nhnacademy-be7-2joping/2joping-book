package com.nhnacademy.bookstore.imageset.repository;

import com.nhnacademy.bookstore.imageset.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
