package com.nhnacademy.bookstore.bookset.publisher.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherAlreadyExistException;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterPublisher_Success() {
        // given
        PublisherRequestDto requestDto = new PublisherRequestDto("출판사 이름");
        Publisher savedPublisher = new Publisher(1L, "출판사 이름");

        when(publisherRepository.findByName("출판사 이름")).thenReturn(Optional.empty());
        when(publisherRepository.save(any(Publisher.class))).thenReturn(savedPublisher);

        // when
        PublisherCreateResponseDto responseDto = publisherService.registerPublisher(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.id());
        assertEquals("출판사 이름", responseDto.name());
    }

    @Test
    public void testRegisterPublisher_AlreadyExists() {
        // given
        PublisherRequestDto requestDto = new PublisherRequestDto("중복된 출판사");
        Publisher existingPublisher = new Publisher(1L, "중복된 출판사");

        when(publisherRepository.findByName("중복된 출판사")).thenReturn(Optional.of(existingPublisher));

        // when & then
        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.registerPublisher(requestDto));
    }

    @Test
    public void testGetPublisherById_Success() {
        // given
        Publisher publisher = new Publisher(1L, "출판사 이름");
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        // when
        PublisherResponseDto responseDto = publisherService.getPublisherById(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.id());
        assertEquals("출판사 이름", responseDto.name());
    }

    @Test
    public void testGetPublisherById_NotFound() {
        // given
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisherById(1L));
    }

    @Test
    public void testGetAllPublishers() {
        // given
        List<Publisher> publishers = Arrays.asList(
                new Publisher(1L, "출판사1"),
                new Publisher(2L, "출판사2")
        );

        when(publisherRepository.findAll()).thenReturn(publishers);

        // when
        List<PublisherResponseDto> responseDtos = publisherService.getAllPublishers();

        // then
        assertEquals(2, responseDtos.size());
        assertEquals("출판사1", responseDtos.get(0).name());
        assertEquals("출판사2", responseDtos.get(1).name());
    }

    @Test
    public void testDeletePublisher_Success() {
        // given
        Publisher publisher = new Publisher(1L, "출판사 이름");
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        doNothing().when(publisherRepository).delete(publisher);

        // when
        publisherService.deletePublisher(1L);

        // then
        verify(publisherRepository, times(1)).delete(publisher);
    }

    @Test
    public void testUpdatePublisher_Success() {
        // given
        Publisher publisher = new Publisher(1L, "이전 출판사 이름");
        PublisherRequestDto requestDto = new PublisherRequestDto("새로운 출판사 이름");

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        // when
        PublisherResponseDto responseDto = publisherService.updatePublisher(1L, requestDto);

        // then
        assertEquals("새로운 출판사 이름", responseDto.name());
    }

    @Test
    public void testUpdatePublisher_NotFound() {
        // given
        PublisherRequestDto requestDto = new PublisherRequestDto("출판사 이름");
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PublisherNotFoundException.class, () -> publisherService.updatePublisher(1L, requestDto));
    }
}

