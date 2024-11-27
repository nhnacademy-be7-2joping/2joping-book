package com.nhnacademy.bookstore.imageset.repository;

import com.nhnacademy.bookstore.imageset.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(Long imageId);
}
