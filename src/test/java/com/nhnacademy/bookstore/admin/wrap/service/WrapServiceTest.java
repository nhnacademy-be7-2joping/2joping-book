package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class WrapServiceTest {

    @Mock
    private WrapRepository wrapRepository;

    @InjectMocks
    private WrapServiceImpl wrapService;

    private Wrap wrap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wrap = new Wrap("포장지1", 1000, true);
    }

    @Test
    void createWrap() {
        ReflectionTestUtils.setField(wrap, "wrapId", 1L);

        given(wrapRepository.findByName("포장지1")).willReturn(Optional.empty());
        given(wrapRepository.save(any(Wrap.class))).willReturn(wrap);

        WrapRequestDto requestDto = new WrapRequestDto("포장지1", 1000, true);
        wrapService.createWrap(requestDto);

        verify(wrapRepository).save(any(Wrap.class));
    }


    @Test
    void createWrap_ConflictException() {
        WrapRequestDto requestDto = new WrapRequestDto("포장지2", 1000, true);

        given(wrapRepository.findByName(requestDto.name())).willReturn(Optional.of(wrap));

        assertThatThrownBy(() -> wrapService.createWrap(requestDto))
                .isInstanceOf(WrapAlreadyExistException.class)
                .hasMessageContaining("포장 상품이 이미 존재합니다.");

        verify(wrapRepository, never()).save(any(Wrap.class)); // 호출되지 않은 것을 확인
    }

    @Test
    void getWrap_Success() {
        ReflectionTestUtils.setField(wrap, "wrapId", 1L);
        given(wrapRepository.findById(1L)).willReturn(Optional.of(wrap));

        WrapResponseDto responseDto = wrapService.getWrap(1L);

        assertThat(responseDto.wrapId()).isEqualTo(1L);
        assertThat(responseDto.name()).isEqualTo("포장지1");
        assertThat(responseDto.wrapPrice()).isEqualTo(1000);
        assertThat(responseDto.isActive()).isTrue();
    }

    @Test
    void getWrap_NotFoundException() {
        given(wrapRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> wrapService.getWrap(1L))
                .isInstanceOf(WrapNotFoundException.class)
                .hasMessageContaining("해당 포장상품이 없습니다");
    }

    @Test
    void getAllWraps() {
        Wrap wrap1 = new Wrap("포장지1", 1000, true);
        ReflectionTestUtils.setField(wrap1, "wrapId", 1L);

        Wrap wrap2 = new Wrap("포장지2", 2000, false);
        ReflectionTestUtils.setField(wrap2, "wrapId", 2L);

        given(wrapRepository.getAllWraps()).willReturn(List.of(
                new WrapResponseDto(1L, "포장지1", 1000, true),
                new WrapResponseDto(2L, "포장지2", 2000, false)
        ));

        List<WrapResponseDto> responseDtos = wrapService.getAllWraps();

        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).name()).isEqualTo("포장지1");
        assertThat(responseDtos.get(1).name()).isEqualTo("포장지2");
    }


    @Test
    void updateWrap_Success() {
        WrapRequestDto requestDto = new WrapRequestDto("수정된 포장지", 1500, false);
        given(wrapRepository.findById(1L)).willReturn(Optional.of(wrap));
        given(wrapRepository.save(any(Wrap.class))).willReturn(wrap);

        WrapResponseDto responseDto = wrapService.updateWrap(1L, requestDto);

        assertThat(responseDto.name()).isEqualTo("수정된 포장지");
        assertThat(responseDto.wrapPrice()).isEqualTo(1500);
        assertThat(responseDto.isActive()).isFalse();
    }

    @Test
    void updateWrap_NotFoundException() {
        WrapRequestDto requestDto = new WrapRequestDto("수정된 포장지", 1500, false);
        given(wrapRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> wrapService.updateWrap(1L, requestDto))
                .isInstanceOf(WrapNotFoundException.class)
                .hasMessageContaining("해당 포장상품이 없습니다.");
    }

    @Test
    void deleteWrap_Success() {
        given(wrapRepository.findById(1L)).willReturn(Optional.of(wrap));

        wrapService.deleteWrap(1L);

        verify(wrapRepository).deleteById(1L);
    }

    @Test
    void deleteWrap_NotFoundException() {
        given(wrapRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> wrapService.deleteWrap(1L))
                .isInstanceOf(WrapNotFoundException.class)
                .hasMessageContaining("해당 포장상품이 없습니다.");
    }
}
