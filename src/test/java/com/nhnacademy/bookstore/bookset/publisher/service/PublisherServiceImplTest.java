package com.nhnacademy.bookstore.bookset.publisher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherAlreadyExistException;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private PublisherRequestDto publisherRequestDto;
    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisherRequestDto = new PublisherRequestDto("Test Publisher");
        publisher = new Publisher(1L, "Test Publisher");
    }

    @Test
    @DisplayName("출판사 등록 - 성공")
    void registerPublisher_Success() {
        // Given
        when(publisherRepository.findByName(publisherRequestDto.name())).thenReturn(Optional.empty());
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        // When
        PublisherCreateResponseDto responseDto = publisherService.registerPublisher(publisherRequestDto);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(publisher.getPublisherId());
        assertThat(responseDto.name()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판사 등록 - 예외 처리")
    void registerPublisher_AlreadyExists() {
        // Given
        when(publisherRepository.findByName(publisherRequestDto.name())).thenReturn(Optional.of(publisher));

        // Then
        assertThatThrownBy(() -> publisherService.registerPublisher(publisherRequestDto))
                .isInstanceOf(PublisherAlreadyExistException.class)
                .hasMessageContaining("등록하려는 출판사가 이미 존재합니다.");
    }

    @Test
    @DisplayName("특정 출판사 조회 - 성공")
    void getPublisherById_Success() {
        // Given
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.of(publisher));

        // When
        PublisherResponseDto responseDto = publisherService.getPublisherById(publisher.getPublisherId());

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(publisher.getPublisherId());
        assertThat(responseDto.name()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("특정 출판사 조회 - 예외 처리")
    void getPublisherById_NotExist() {
        // Given
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> publisherService.getPublisherById(publisher.getPublisherId()))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessageContaining("출판사를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("전체 출판사 조회")
    void getAllPublishers_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PublisherResponseDto publisherResponseDto = new PublisherResponseDto(publisher.getPublisherId(), publisher.getName());
        Page<PublisherResponseDto> page = new PageImpl<>(List.of(publisherResponseDto));
        when(publisherRepository.findAllBy(pageable)).thenReturn(page);

        // When
        Page<PublisherResponseDto> result = publisherService.getAllPublishers(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(publisher.getPublisherId());
        assertThat(result.getContent().get(0).name()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판사 삭제 - 성공")
    void deletePublisher_Success() {
        // Given
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.of(publisher));

        // When
        publisherService.deletePublisher(publisher.getPublisherId());

        // Then
        verify(publisherRepository, times(1)).delete(publisher);
    }

    @Test
    @DisplayName("출판사 삭제 - 예외 처리")
    void deletePublisher_NotExist() {
        // Given
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> publisherService.deletePublisher(publisher.getPublisherId()))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessageContaining("출판사를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("출판사 수정 - 성공")
    void updatePublisher_Success() {
        // Given
        Publisher updatedPublisher = new Publisher(1L, "Updated Publisher");
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(updatedPublisher);

        // When
        PublisherResponseDto responseDto = publisherService.updatePublisher(publisher.getPublisherId(), new PublisherRequestDto("Updated Publisher"));

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(updatedPublisher.getPublisherId());
        assertThat(responseDto.name()).isEqualTo(updatedPublisher.getName());
    }

    @Test
    @DisplayName("출판사 수정 - 예외 처리")
    void updatePublisher_NotExist() {
        // Given
        when(publisherRepository.findById(publisher.getPublisherId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> publisherService.updatePublisher(publisher.getPublisherId(), publisherRequestDto))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessageContaining("출판사를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("전체 출판사 등록용 조회 - 성공")
    void getAllPublishersForRegister_Success() {
        // Given
        Publisher publisher1 = new Publisher(1L, "Publisher 1");
        Publisher publisher2 = new Publisher(2L, "Publisher 2");
        List<Publisher> publishers = List.of(publisher1, publisher2);

        when(publisherRepository.findAll()).thenReturn(publishers);

        // When
        List<PublisherResponseDto> responseDtoList = publisherService.getAllPublishersForRegister();

        // Then
        assertThat(responseDtoList).isNotEmpty();
        assertThat(responseDtoList).hasSize(2);
        assertThat(responseDtoList.get(0).id()).isEqualTo(publisher1.getPublisherId());
        assertThat(responseDtoList.get(0).name()).isEqualTo(publisher1.getName());
        assertThat(responseDtoList.get(1).id()).isEqualTo(publisher2.getPublisherId());
        assertThat(responseDtoList.get(1).name()).isEqualTo(publisher2.getName());
    }

}