package com.nhnacademy.bookstore.admin.wrap.repository;

import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WrapRepository extends JpaRepository<Wrap, Long>, WrapRepositoryCustom {
    List<Wrap> findByWrapIdIn(List<Long> wrapIds);

    List<Wrap> findByWrapIdIn(Set<Long> wrapIds);

    Optional<Wrap> findByName(String name);

    @Query("SELECT new com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto(" +
            "w.wrapId, w.name, w.wrapPrice, w.isActive, i.url) " +
            "FROM Wrap w " +
            "JOIN WrapImage wi ON w.wrapId = wi.wrap.wrapId " +
            "JOIN Image i ON wi.image.imageId = i.imageId")
    List<WrapUpdateResponseDto> findAllWrapsWithImages();
}
