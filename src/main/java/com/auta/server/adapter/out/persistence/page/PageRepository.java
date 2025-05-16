package com.auta.server.adapter.out.persistence.page;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {
    Optional<PageEntity> findById(Long pageId);

    @Query("""
                select p from PageEntity as p
                where p.projectEntity.id = :projectId
            """)
    List<PageEntity> findAllByProjectId(@Param("projectId") Long projectId);
}
