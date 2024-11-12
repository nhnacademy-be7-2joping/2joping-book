package com.nhnacademy.bookstore.bookset.publisher.repository;

import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher,Long>, PublisherRepositoryCustom {
    Optional<Publisher> findByName(String name);

}
