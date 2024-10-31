package com.nhnacademy.bookstore.bookset.publisher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 출판사 Response DTO
 *
 * @author : 이유현
 * @date : 2024.10.23
 */

@Getter
@AllArgsConstructor
public class PublisherResponseDto {

    private Long id; // 서버한테 보내는 용도
    private String name;
}
