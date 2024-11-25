package com.nhnacademy.bookstore.bookset.publisher.service;
/**
 * 출판사 Service
 *
 * @author : 이유현
 * @date : 2024.10.23
 */
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {


    PublisherCreateResponseDto registerPublisher(PublisherRequestDto requestDto);

    PublisherResponseDto getPublisherById(Long id);

    Page<PublisherResponseDto> getAllPublishers(Pageable pageable);

    List<PublisherResponseDto> getAllPublishersForRegister();

    PublisherResponseDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto);

    void deletePublisher(Long id);

}
