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

//
//    //출판사 생성 필요한지 고민해보기 (등록만 있어도 되는건가?)
//    PublisherCreateResponseDto createPublisher(PublisherRequestDto requestDto);

    //출판사 등록
    PublisherCreateResponseDto registerPublisher(PublisherRequestDto requestDto);

    //출판사 선택 조회
    PublisherResponseDto getPublisherById(Long id);

    //출판사 전체 조회
    List<PublisherResponseDto> getAllPublishers();

    //출판사 업데이트
    PublisherResponseDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto);

    //출판사 삭제
    void deletePublisher(Long id);

}
