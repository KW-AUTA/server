package com.auta.server.adapter.out.persistence.ui;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UITestRepository extends JpaRepository<UITestEntity, Long> {

    @Query("""
                select ut from UITestEntity as ut
                join fetch ut.projectEntity p
                where p.id = :projectId
            """)
    List<UITestEntity> findAllByProjectId(@Param("projectId") Long projectId);
}
