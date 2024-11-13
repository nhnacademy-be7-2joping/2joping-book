package com.nhnacademy.bookstore.imageset.repository;

import com.nhnacademy.bookstore.imageset.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
