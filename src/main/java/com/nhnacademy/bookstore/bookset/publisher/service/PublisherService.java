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

import java.util.List;

public interface PublisherService {


    PublisherCreateResponseDto registerPublisher(PublisherRequestDto requestDto);

    PublisherResponseDto getPublisherById(Long id);

    List<PublisherResponseDto> getAllPublishers();

    PublisherResponseDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto);

    void deletePublisher(Long id);

}
