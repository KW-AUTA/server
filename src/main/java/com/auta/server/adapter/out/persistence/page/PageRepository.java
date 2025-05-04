package com.auta.server.adapter.out.persistence.page;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {
    Optional<PageEntity> findById(Long pageId);
}
