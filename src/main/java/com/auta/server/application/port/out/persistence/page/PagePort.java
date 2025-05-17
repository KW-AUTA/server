package com.auta.server.application.port.out.persistence.page;

import com.auta.server.domain.page.Page;
import java.util.List;
import java.util.Optional;

public interface PagePort {
    Optional<Page> findById(Long pageId);

    List<Page> findAllByProjectId(Long projectId);
}
