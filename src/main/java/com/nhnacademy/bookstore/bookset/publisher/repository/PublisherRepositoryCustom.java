package com.nhnacademy.bookstore.bookset.publisher.repository;

import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherRepositoryCustom {

    Page<PublisherResponseDto> findAllBy(Pageable pabeable);
}
