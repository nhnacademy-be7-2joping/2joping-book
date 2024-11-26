package com.nhnacademy.bookstore.bookset.contributor.entity;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 기여자 역할 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contributor_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class ContributorRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributorRoleId;

    @Column(length = 50)
    private String name;

    public void toEntity(ContributorRoleRequestDto requestDto){
        this.name = requestDto.name();
    }
}
