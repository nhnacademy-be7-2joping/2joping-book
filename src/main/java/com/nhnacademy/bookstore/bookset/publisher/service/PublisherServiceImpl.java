package com.nhnacademy.bookstore.bookset.publisher.service;

    /**
     * 출판사 ServiceImpl
     *
     * @author : 이유현
     *
     * @date : 2024.10.23
     */

import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherAlreadyExistException;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService{

    private final PublisherRepository publisherRepository;

    private Publisher findPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("출판사를 찾을 수 없습니다."));
    }

    /**
     * 출판사 정보를 등록하는 메서드
     * @param requestDto  출판사 이름을 체크해서 DB에 저장. 이미 있으면 PublisherAlreadyExistException 발생
     * @return -> PublisherCreateResponseDto
     */
    @Override
    public PublisherCreateResponseDto registerPublisher(PublisherRequestDto requestDto) {
        publisherRepository.findByName(requestDto.name())
                .ifPresent(existingPublisher -> {
                    throw new PublisherAlreadyExistException("등록하려는 출판사가 이미 존재합니다.");
                });

        Publisher publisher = new Publisher(requestDto.name());
        Publisher savedPublisher = publisherRepository.save(publisher);

        return new PublisherCreateResponseDto(savedPublisher.getPublisherId(), savedPublisher.getName());
    }


    /**
     * 특정 출판사를 조회하는 메서드
     * @param id 조회하려는 publisher의 id, 없으면 PublisherNotFoundException 발생
     * @return -> 출판사 객체
     */
    @Override
    @Transactional(readOnly = true)
    public PublisherResponseDto getPublisherById(Long id) {
        Publisher publisher = findPublisherById(id);
        return new PublisherResponseDto(publisher.getPublisherId(), publisher.getName());
    }

    /**
     * 모든 출판사를 조회하는 메서드
     * @return -> 출판사 객체
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PublisherResponseDto> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAllBy(pageable);
    }


    /**
     * 모든 출판사를 조회하는 메서드 (등록용)
     * @return -> 출판사 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<PublisherResponseDto> getAllPublishersForRegister() {
        List<Publisher> publishers = publisherRepository.findAll();
        List<PublisherResponseDto> responseDtoList = new ArrayList<>();

        for (Publisher publisher : publishers) {
            PublisherResponseDto dto = new PublisherResponseDto(publisher.getPublisherId(), publisher.getName());
            responseDtoList.add(dto);
        }
        return responseDtoList;
    }


    /**
     * 특정 출판사를 삭제하는 메서드
     * @param id 삭제하려는 publisher의 id, 없으면 PublisherNotFoundException 발생
     */
    @Override
    public void deletePublisher(Long id) {
        Publisher publisher = findPublisherById(id);
        publisherRepository.delete(publisher);
    }

    /**
     * 특정 출판사를 수정하는 메서드
     * @param id 수정하려는 publisher의 id, 없으면 PublisherNotFoundException 발생
     * @return -> PublisherResponseDto
     */
    @Override
    public PublisherResponseDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto) {
        Publisher publisher = findPublisherById(id);
        publisher.update(publisherRequestDto.name());
        Publisher updatedPublisher = publisherRepository.save(publisher);
        return new PublisherResponseDto(updatedPublisher.getPublisherId(), updatedPublisher.getName());
    }
}


