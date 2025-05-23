package com.auta.server.application.port.out.persistence.test;

import com.auta.server.domain.test.Test;
import java.util.List;

public interface TestPort {
    List<Test> findAllByPageId(Long pageId);

    List<Test> findAllByProjectId(Long projectId);

    List<Test> findAllByProjectIdInOrderByCreationTimeDesc(List<Long> projectIds);

    void deleteAllByProjectId(Long projectId);
}
