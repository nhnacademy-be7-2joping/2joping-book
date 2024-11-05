package com.nhnacademy.bookstore.point.entity;

import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "point_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointTypeEnum type;

    @Column(name = "acc_val", nullable = false)
    private Integer accVal;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "pointType")
    private List<PointHistory> pointHistories = new ArrayList<>();
}