package com.nhnacademy.bookstore.bookset.publisher.controller;

import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherCreateResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.request.PublisherRequestDto;
import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * 출판사 Controller
 *
 * @author : 이유현
 * @date : 2024.10.23
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookstore")
@Tag(name = "Publisher API", description = "출판사 관련 CRUD API")
public class PublisherController {

    private final PublisherService publisherService;

    /**
     * 출판사를 등록하는 controller
     * @param publisherRequestDto 유효성 검사를 거친 출판사 등록 정보
     * @param bindingResult 유효성 검사 결과
     * @return 등록된 출판사의 정보와 상태 코드를 담은 응답
     */
    @Operation(summary = "출판사 등록", description = "새로운 출판사를 등록합니다.")
    @PostMapping("/publishers")
    public ResponseEntity<PublisherCreateResponseDto> registerPublisher(
            @Valid @RequestBody @Parameter(description = "등록할 출판사 정보") PublisherRequestDto publisherRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        PublisherCreateResponseDto registeredPublisher = publisherService.registerPublisher(publisherRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredPublisher);
    }

    /**
     * 전체 출판사를 조회하는 controller
     * @return 전체 줄판사와 상태 코드를 담은 응답
     */
    @Operation(summary = "전체 출판사 조회", description = "등록된 모든 출판사를 조회합니다.")
    @GetMapping("/publishers")
    public ResponseEntity<Page<PublisherResponseDto>> getAllPublishers(Pageable pageable) {
        Page<PublisherResponseDto> publishers = publisherService.getAllPublishers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    /**
     * 전체 출판사를 조회하는 controller (등록용)
     * @return 전체 줄판사와 상태 코드를 담은 응답
     */
    @Operation(summary = "전체 출판사 조회 (등록용)", description = "등록된 모든 출판사를 조회합니다.")
    @GetMapping("/publishers/list")
    public ResponseEntity<List<PublisherResponseDto>> getAllPublishersForRegister() {
        List<PublisherResponseDto> publishers = publisherService.getAllPublishersForRegister();
        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    /**
     * 특정 출판사를 조회하는 controller
     * @param id 특정 출판사를 조회할 id
     * @return 특정 줄판사와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 출판사 조회", description = "ID로 특정 출판사를 조회합니다.")
    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisher(
            @Parameter(description = "조회할 출판사 ID") @PathVariable Long id) {
        PublisherResponseDto getPublisher = publisherService.getPublisherById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getPublisher);
    }

    /**
     * 특정 출판사를 삭제하는 controller
     * @param id 특정 출판사를 조회할 id
     * @return 본문이 없는 상태 코드 응답 (204 No Content)
     */
    @Operation(summary = "특정 출판사 삭제", description = "ID로 특정 출판사를 삭제합니다.")
    @DeleteMapping("/publisher/{id}")
    public ResponseEntity<Void> deletePublisher(
            @Parameter(description = "삭제할 출판사 ID") @PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 특정 출판사를 update하는 controller
     * @param id 특정 출판사를 조회할 id
     * @return update한 출판사와 상태 코드를 담은 응답
     */
    @Operation(summary = "특정 출판사 수정", description = "ID로 특정 출판사 정보를 수정합니다.")
    @PutMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(
            @Parameter(description = "수정할 출판사 ID") @PathVariable Long id,
            @Valid @RequestBody @Parameter(description = "수정할 출판사 정보") PublisherRequestDto publisherRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        PublisherResponseDto updatePublisher = publisherService.updatePublisher(id, publisherRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatePublisher);
    }
}

