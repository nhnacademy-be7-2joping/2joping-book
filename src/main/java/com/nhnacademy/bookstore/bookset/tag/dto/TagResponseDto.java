package com.nhnacademy.bookstore.bookset.tag.dto;
import lombok.*;

/**
 * 태그 정보를 반환하는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagResponseDto {
    private Long tagId;
    private String name;
}
