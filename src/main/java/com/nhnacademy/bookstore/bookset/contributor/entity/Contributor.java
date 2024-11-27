package com.nhnacademy.bookstore.bookset.contributor.entity;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 기여자 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contributor", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"contributor_role_id", "name"})
})
public class Contributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributorId;

    @ManyToOne
    @JoinColumn(name = "contributor_role_id")
    private ContributorRole contributorRole;

    @Column(length = 30)
    private String name;

    @Column
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void toEntity(ContributorRequestDto requestDto, ContributorRole contributorRole) {
        this.contributorRole = contributorRole;
        this.name = requestDto.name();
        this.isActive = true;
    }
}
