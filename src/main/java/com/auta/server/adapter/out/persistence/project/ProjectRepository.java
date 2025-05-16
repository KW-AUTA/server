package com.auta.server.adapter.out.persistence.project;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectQueryRepository {
    @Query("""
            select p from ProjectEntity p
            join fetch p.userEntity u
            where u.id = :userId
            """)
    List<ProjectEntity> findAllByUserId(@Param("userId") Long userId);
}
