package com.nhnacademy.bookstore.bookset.contributor.entity;

/**
 * 도서 기여자 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    @JoinColumn(name = "contributor_role_id", nullable = false)
    private ContributorRole contributorRole;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Boolean isActive = true;
}
