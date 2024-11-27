package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.request.*;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.review.RatingValueNotValidException;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.entity.ReviewImage;
import com.nhnacademy.bookstore.imageset.entity.WrapImage;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.WrapImageRepository;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * 포장 상품 서비스 구현
 * 포장 상품 생성, 조회, 수정, 삭제 기능을 제공합니다.
 * <p>
 * 작성자: 박채연
 * 작성일: 2024-11-06
 */
@Service
@RequiredArgsConstructor
public class WrapServiceImpl implements WrapService {
    private final WrapRepository wrapRepository;
    private final ImageRepository imageRepository;
    private final WrapImageRepository wrapImageRepository;

    /**
     * 새로운 포장 상품을 생성합니다.
     *
     * @param requestDto 포장 상품 요청 DTO
     * @throws WrapAlreadyExistException 동일한 이름의 포장 상품이 이미 존재하는 경우
     */
    @Override
    @Transactional
    public WrapCreateResponseDto createWrap(WrapRequestDto requestDto) {
        WrapDetailRequestDto wrapDetailRequestDto = requestDto.wrapDetailRequestDto();
        WrapImageUrlRequestDto wrapImageUrlRequestDto = requestDto.imageUrlRequestDto();
        if (wrapRepository.findByName(wrapDetailRequestDto.name()).isPresent()) {
            throw new WrapAlreadyExistException();
        }
        Wrap wrap = new Wrap(
                wrapDetailRequestDto.name(),
                wrapDetailRequestDto.wrapPrice(),
                wrapDetailRequestDto.isActive()
        );
        String wrapImageUrl = wrapImageUrlRequestDto.wrapImageUrl();
        wrapRepository.save(wrap);
        if (wrapImageUrlRequestDto.wrapImageUrl() != null) {
            Image image = imageRepository.save(new Image(wrapImageUrl));
            wrapImageRepository.save(new WrapImage(wrap,image));
        }
        return new WrapCreateResponseDto(wrap.getWrapId(), wrap.getName(), wrap.getWrapPrice(), wrap.isActive(), wrapImageUrl);
    }

    /**
     * 포장 상품을 ID로 조회합니다.
     *
     * @param wrapId 포장 상품의 ID
     * @return 포장 상품 응답 DTO
     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
     */
    @Override
    public WrapUpdateResponseDto getWrap(Long wrapId) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(WrapNotFoundException::new);
        WrapImage wrapImage = wrapImageRepository.findFirstByWrap_WrapId(wrap.getWrapId()).get();
        Image image = imageRepository.findById(wrapImage.getImage().getImageId()).get();
        String wrapImageUrl = image.getUrl();

        return new WrapUpdateResponseDto(
                wrap.getWrapId(),
                wrap.getName(),
                wrap.getWrapPrice(),
                wrap.isActive(),
                wrapImageUrl);
    }

    /**
     * 포장 상품을 업데이트합니다.
     *
     * @param wrapId 업데이트할 포장 상품의 ID
     * @param dto    업데이트할 포장 상품 데이터
     * @return 업데이트된 포장 상품 응답 DTO
     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
     */
    @Override
    public WrapResponseDto updateWrap(Long wrapId, WrapModifyRequestDto dto) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(WrapNotFoundException::new);

        wrap.updateWrap(
                dto.name(),
                dto.wrapPrice(),
                dto.isActive()
        );

        Wrap updatedWrap = wrapRepository.save(wrap);
        return new WrapResponseDto(updatedWrap.getWrapId(), updatedWrap.getName(), updatedWrap.getWrapPrice(), updatedWrap.isActive());
    }

    /**
     * 활성화 된 포장 상품을 조회합니다.
     *
     * @return 포장 상품 응답 DTO 리스트
     */
    @Override // query dsl 사용
    public List<WrapUpdateResponseDto> findAllByIsActiveTrue() {
        return wrapRepository.findAllWrapsWithImages();
    }

//    /**
//     * 포장 상품을 업데이트합니다.
//     *
//     * @param wrapUpdateRequestDto    업데이트할 포장 상품 데이터
//     * @return 업데이트된 포장 상품 응답 DTO
//     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
//     */
//    @Override
//    public WrapUpdateResponseDto updateWrap(Long wrapId, WrapUpdateRequestDto wrapUpdateRequestDto) {
//        WrapUpdateDetailRequestDto wrapUpdateDetailRequestDto = wrapUpdateRequestDto.wrapUpdateDetailRequestDto();
//        WrapImageUrlRequestDto wrapImageUrlRequestDto = wrapUpdateRequestDto.wrapImageUrlRequestDto();
//
//        Wrap wrap = wrapRepository.findById(wrapId)
//                .orElseThrow(WrapNotFoundException::new);
//
//        wrap.updateWrap(
//                wrapUpdateDetailRequestDto.name(),
//                wrapUpdateDetailRequestDto.wrapPrice(),
//                wrapUpdateDetailRequestDto.isActive()
//        );
//
//        String updatedUrl = null;
//        if (wrapUpdateRequestDto.deleteImage()) {
//            wrapImageRepository.deleteByWrap_WrapId(wrap.getWrapId());
//        } else if (wrapImageUrlRequestDto.wrapImageUrl() != null) {
//            wrapImageRepository.deleteByWrap_WrapId(wrap.getWrapId());
//            Image newImage = imageRepository.save(new Image(wrapImageUrlRequestDto.wrapImageUrl()));
//            wrapImageRepository.save(new WrapImage(wrap, newImage));
//            updatedUrl = newImage.getUrl();
//        }
//        Wrap updatedWrap = wrapRepository.save(wrap);
//        return new WrapUpdateResponseDto(updatedWrap.getWrapId(), updatedWrap.getName(), updatedWrap.getWrapPrice(), updatedWrap.isActive(), updatedUrl);
//    }

    /**
     * 포장 상품을 ID로 삭제합니다.
     *
     * @param wrapId 삭제할 포장 상품의 ID
     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
     */
    @Override
    public void deleteWrap(Long wrapId) {
        wrapRepository.findById(wrapId)
                .orElseThrow(WrapNotFoundException::new);
        wrapRepository.deleteById(wrapId);
    }
}


