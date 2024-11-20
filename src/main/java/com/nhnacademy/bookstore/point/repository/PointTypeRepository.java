package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointTypeRepository extends JpaRepository<PointType, Long>, PointTypeRepositoryCustom {

    Optional<PointType> findByNameAndIsActiveTrue(String name);
}
