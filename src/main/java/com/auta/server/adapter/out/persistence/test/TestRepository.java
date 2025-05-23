package com.auta.server.adapter.out.persistence.test;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    @Query("""
                select t from TestEntity as t
                where t.pageEntity.id = :pageId
            """)
    List<TestEntity> findAllByPageId(@Param("pageId") Long pageId);

    @Query("""
                select t from TestEntity as t
                join fetch t.projectEntity p
                where p.id = :projectId
            """)
    List<TestEntity> findAllByProjectId(@Param("projectId") Long projectId);

    @Query("""
                select t from TestEntity as t
                join fetch t.projectEntity p
                where p.id in :projectIds
                order by t.createdDateTime desc
            """)
    List<TestEntity> findAllByProjectIdInOrderByCreatedTime(@Param("projectIds") List<Long> projectIds);

    @Modifying
    @Query("""
                delete from TestEntity as t
                where t.projectEntity.id = :projectId
            """)
    void deleteAllByProjectId(@Param("projectId") Long projectId);
}
