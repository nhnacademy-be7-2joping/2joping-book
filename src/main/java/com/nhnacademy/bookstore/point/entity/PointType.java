package com.nhnacademy.bookstore.point.entity;

import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.*;

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
    private Long pointTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PointTypeEnum type;

    @Column(name = "acc_val")
    private Integer accVal;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_active")
    private boolean isActive = true;


    @Builder
    public PointType(PointTypeEnum type, Integer accVal, String name, Boolean isActive) {
        this.type = type;
        this.accVal = accVal;
        this.name = name;
        this.isActive = isActive;
    }

    public void updateAccVal(Integer accVal) {
        this.accVal = accVal;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updatePointType(PointTypeEnum type, Integer accVal, String name, boolean isActive) {
        this.type = type;
        this.accVal = accVal;
        this.name = name;
        this.isActive = isActive;
    }

}
