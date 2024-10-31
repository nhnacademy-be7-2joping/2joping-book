package com.nhnacademy.bookstore.wrap.entity;
/**
 * 포장 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wrap")
public class Wrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_id", nullable = false)
    private Long wrapId;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "wrap_price", nullable = false)
    private int wrapPrice;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
