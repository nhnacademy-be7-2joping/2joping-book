package com.nhnacademy.bookstore.bookset.publisher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 출판사 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    @Column(nullable = false, length = 50)
    private String name;

    // 생성자 추가 (이유현)
    public Publisher(String name){
        this.name = name;
    }
    //update 메서드 추가 (이유현)
    public void update(String name) {
        this.name = name;
    }

}
