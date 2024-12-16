package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.request.*;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapImageNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.entity.WrapImage;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.WrapImageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WrapServiceTest {

    @Mock
    private WrapRepository wrapRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private WrapImageRepository wrapImageRepository;

    @InjectMocks
    private WrapServiceImpl wrapService;

    WrapServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWrap_success() {
        // Given
        WrapRequestDto requestDto = mock(WrapRequestDto.class);
        WrapDetailRequestDto detailDto = mock(WrapDetailRequestDto.class);
        WrapImageUrlRequestDto imageUrlDto = mock(WrapImageUrlRequestDto.class);

        when(requestDto.wrapDetailRequestDto()).thenReturn(detailDto);
        when(requestDto.imageUrlRequestDto()).thenReturn(imageUrlDto);
        when(detailDto.name()).thenReturn("포장 상품");
        when(detailDto.wrapPrice()).thenReturn(1000);
        when(detailDto.isActive()).thenReturn(true);
        when(imageUrlDto.wrapImageUrl()).thenReturn("http://image.com/image.jpg");
        when(wrapRepository.findByName("포장 상품")).thenReturn(Optional.empty());

        // Mock Wrap entity
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        when(wrapRepository.save(any(Wrap.class))).thenReturn(mockWrap);

        // Mock Image entity
        Image mockImage = new Image("http://image.com/image.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        // When
        WrapCreateResponseDto response = wrapService.createWrap(requestDto);

        // Then
        assertNotNull(response);
        assertEquals("포장 상품", response.name());
        assertEquals(1000, response.wrapPrice());
        assertTrue(response.isActive());
        assertEquals("http://image.com/image.jpg", response.wrapImage());
    }


    @Test
    void getWrap_Success() {
        // Given
        Long wrapId = 1L;

        // Mock Wrap entity
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        // Set wrapId for mockWrap
        ReflectionTestUtils.setField(mockWrap, "wrapId", wrapId);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(mockWrap));

        // Mock Image entity
        Image mockImage = new Image("http://image.com/image.jpg");
        // Set imageId for mockImage
        ReflectionTestUtils.setField(mockImage, "imageId", 2L);

        // Mock WrapImage entity
        WrapImage mockWrapImage = new WrapImage(mockWrap, mockImage);
        when(wrapImageRepository.findFirstByWrap_WrapId(wrapId)).thenReturn(Optional.of(mockWrapImage));
        when(imageRepository.findById(mockImage.getImageId())).thenReturn(Optional.of(mockImage));

        // When
        WrapUpdateResponseDto response = wrapService.getWrap(wrapId);

        // Then
        assertNotNull(response);
        assertEquals(wrapId, response.wrapId());
        assertEquals("포장 상품", response.name());
        assertEquals(1000, response.wrapPrice());
        assertTrue(response.isActive());
        assertEquals("http://image.com/image.jpg", response.wrapImage());

        // Verify
        verify(wrapRepository, times(1)).findById(wrapId);
        verify(wrapImageRepository, times(1)).findFirstByWrap_WrapId(wrapId);
        verify(imageRepository, times(1)).findById(mockImage.getImageId());
    }


    @Test
    void createWrap_ConflictException() {
        // Given
        WrapRequestDto requestDto = mock(WrapRequestDto.class);
        WrapDetailRequestDto detailDto = mock(WrapDetailRequestDto.class);

        when(requestDto.wrapDetailRequestDto()).thenReturn(detailDto);
        when(detailDto.name()).thenReturn("중복 상품");
        when(wrapRepository.findByName("중복 상품")).thenReturn(Optional.of(new Wrap("중복 상품", 1000, true)));

        // When & Then
        assertThrows(WrapAlreadyExistException.class, () -> wrapService.createWrap(requestDto));

        verify(wrapRepository, never()).save(any(Wrap.class)); // save() 메서드가 호출되지 않음을 확인
    }


    @Test
    void getWrap_WrapNotFound() {
        // Given
        Long wrapId = 999L;

        // Mock Wrap not found
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WrapNotFoundException.class, () -> wrapService.getWrap(wrapId));
        verify(wrapRepository, times(1)).findById(wrapId);
    }

    @Test
    void getWrap_NotFoundException() {
        // Given
        Long wrapId = 1L;
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WrapNotFoundException.class, () -> wrapService.getWrap(wrapId));
    }

    @Test
    void findAllByIsActiveTrue() {
        // Given
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        Image mockImage = new Image("http://image.com/image.jpg");
        WrapImage mockWrapImage = new WrapImage(mockWrap, mockImage);

        when(wrapRepository.findAllWrapsWithImages()).thenReturn(List.of(
                new WrapUpdateResponseDto(1L, "포장 상품", 1000, true, "http://image.com/image.jpg")
        ));

        // When
        List<WrapUpdateResponseDto> responseList = wrapService.findAllByIsActiveTrue();

        // Then
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        WrapUpdateResponseDto response = responseList.get(0);
        assertEquals("포장 상품", response.name());
        assertEquals(1000, response.wrapPrice());
        assertTrue(response.isActive());
        assertEquals("http://image.com/image.jpg", response.wrapImage());
    }

    @Test
    void updateWrap_Success() {
        // Given
        Long wrapId = 1L;
        WrapUpdateRequestDto updateRequestDto = mock(WrapUpdateRequestDto.class);
        WrapUpdateDetailRequestDto detailDto = mock(WrapUpdateDetailRequestDto.class);
        WrapImageUrlRequestDto imageUrlDto = mock(WrapImageUrlRequestDto.class);

        when(updateRequestDto.wrapUpdateDetailRequestDto()).thenReturn(detailDto);
        when(updateRequestDto.wrapImageUrlRequestDto()).thenReturn(imageUrlDto);
        when(detailDto.name()).thenReturn("수정된 포장 상품");
        when(detailDto.wrapPrice()).thenReturn(2000);
        when(detailDto.isActive()).thenReturn(false);
        when(imageUrlDto.wrapImageUrl()).thenReturn("http://image.com/new-image.jpg");

        Wrap existingWrap = new Wrap("포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(existingWrap));

        Image newImage = new Image("http://image.com/new-image.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(newImage);

        // When
        WrapUpdateResponseDto response = wrapService.updateWrap(wrapId, updateRequestDto);

    }

    @Test
    void updateWrap_Success_WithImageUpdate() {
        // Given
        Long wrapId = 1L;

        // Mock 요청 DTO
        WrapUpdateDetailRequestDto detailDto = new WrapUpdateDetailRequestDto("수정된 포장 상품", 1500, false);
        WrapImageUrlRequestDto imageUrlDto = new WrapImageUrlRequestDto("http://image.com/new-image.jpg");
        WrapUpdateRequestDto requestDto = new WrapUpdateRequestDto(detailDto, imageUrlDto, false);

        // Mock 기존 Wrap
        Wrap existingWrap = new Wrap("기존 포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(existingWrap));
        when(wrapRepository.existsById(wrapId)).thenReturn(true);

        // Mock WrapImage 존재 여부
        when(wrapImageRepository.existsByWrap_WrapId(wrapId)).thenReturn(true);

        // Mock 새 이미지
        Image newImage = new Image("http://image.com/new-image.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(newImage);

        // When
        WrapUpdateResponseDto response = wrapService.updateWrap(wrapId, requestDto);

        // Then
        assertNotNull(response);
        assertEquals("수정된 포장 상품", response.name());
        assertEquals(1500, response.wrapPrice());
        assertFalse(response.isActive());
        assertEquals("http://image.com/new-image.jpg", response.wrapImage());

        // Verify
        verify(wrapImageRepository, times(1)).deleteByWrap_WrapId(wrapId); // 삭제 확인
        verify(wrapImageRepository, times(1)).save(any(WrapImage.class)); // 저장 확인
        verify(wrapRepository, times(1)).save(existingWrap); // Wrap 업데이트 확인
    }

    @Test
    void updateWrap_Success_DeleteImage() {
        // Given
        Long wrapId = 1L;

        // Mock 요청 DTO (이미지 삭제 요청)
        WrapUpdateDetailRequestDto detailDto = new WrapUpdateDetailRequestDto("수정된 포장 상품", 1500, true);
        WrapUpdateRequestDto requestDto = new WrapUpdateRequestDto(detailDto, null, true);

        // Mock 기존 Wrap
        Wrap existingWrap = new Wrap("기존 포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(existingWrap));
        when(wrapRepository.existsById(wrapId)).thenReturn(true);

        // Mock 기본 이미지
        Image defaultImage = new Image("http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(defaultImage);

        // When
        WrapUpdateResponseDto response = wrapService.updateWrap(wrapId, requestDto);

        // Then
        assertNotNull(response);
        assertEquals("수정된 포장 상품", response.name());
        assertEquals(1500, response.wrapPrice());
        assertTrue(response.isActive());
        assertEquals("http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg", response.wrapImage());

        // Verify
        verify(wrapRepository, times(1)).save(existingWrap);
        verify(wrapImageRepository, times(1)).deleteByWrap_WrapId(wrapId);
        verify(wrapImageRepository, times(1)).save(any(WrapImage.class));
    }


    @Test
    void updateWrap_NotFoundException() {
        // Given
        Long wrapId = 1L;
        WrapUpdateRequestDto updateRequestDto = mock(WrapUpdateRequestDto.class);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WrapNotFoundException.class, () -> wrapService.updateWrap(wrapId, updateRequestDto));
    }

    @Test
    void deleteWrap_Success() {
        // Given
        Long wrapId = 1L;
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(mockWrap));
        doNothing().when(wrapRepository).deleteById(wrapId);

        // When
        wrapService.deleteWrap(wrapId);

        // Then
        verify(wrapRepository, times(1)).deleteById(wrapId);
    }

    @Test
    void deleteWrap_NotFoundException() {
        // Given
        Long wrapId = 1L;
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WrapNotFoundException.class, () -> wrapService.deleteWrap(wrapId));
    }


    @Test
    void getWrap_WrapImageNotFound() {
        // Given
        Long wrapId = 1L;
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(mockWrap));
        when(wrapImageRepository.findFirstByWrap_WrapId(wrapId)).thenReturn(Optional.empty());

        // When & Then
        // NoSuchElementException 대신 WrapImageNotFoundException으로 변경
        assertThrows(WrapImageNotFoundException.class, () -> wrapService.getWrap(wrapId));
    }

    @Test
    void getWrap_ImageNotFound() {
        // Given
        Long wrapId = 1L;
        Wrap mockWrap = new Wrap("포장 상품", 1000, true);
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.of(mockWrap));

        WrapImage mockWrapImage = new WrapImage(mockWrap, new Image("http://image.com/image.jpg"));
        when(wrapImageRepository.findFirstByWrap_WrapId(wrapId)).thenReturn(Optional.of(mockWrapImage));

        when(imageRepository.findById(mockWrapImage.getImage().getImageId())).thenReturn(Optional.empty());

        // When & Then
        // NoSuchElementException 대신 WrapImageNotFoundException으로 변경
        assertThrows(WrapImageNotFoundException.class, () -> wrapService.getWrap(wrapId));
    }
}
