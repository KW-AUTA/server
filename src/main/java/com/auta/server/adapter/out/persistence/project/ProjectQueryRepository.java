package com.auta.server.adapter.out.persistence.project;

import java.util.List;

public interface ProjectQueryRepository {
    List<ProjectEntity> findByProjectNameWithPaging(String projectName, String sortBy, Long cursor, int pageSize);
}
