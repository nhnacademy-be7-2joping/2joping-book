package com.nhnacademy.bookstore.bookset.tag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 태그 생성/수정 요청을 위한 DTO
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagRequestDto {
    @NotBlank(message = "태그 이름은 필수 입력 사항입니다.")
    private String name;
}
