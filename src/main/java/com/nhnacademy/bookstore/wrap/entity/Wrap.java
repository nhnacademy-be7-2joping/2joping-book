package com.nhnacademy.bookstore.wrap.entity;
/**
 * 포장 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wrap")
@Getter
@Setter
@NoArgsConstructor
public class Wrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_id", nullable = false)
    private Long wrapId;

    @Column(name = "wrap_name", length = 32, nullable = false)
    private String wrapName;

    @Column(name = "wrap_price", nullable = false)
    private int wrapPrice;
}
