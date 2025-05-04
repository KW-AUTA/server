package com.auta.server.adapter.out.persistence.test;

import com.auta.server.adapter.out.persistence.page.PageMapper;
import com.auta.server.adapter.out.persistence.project.ProjectMapper;
import com.auta.server.domain.test.Test;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class, PageMapper.class})
public interface TestMapper {

    @Mapping(source = "testEntity.projectEntity", target = "project")
    @Mapping(source = "testEntity.pageEntity", target = "page")
    Test toDomain(TestEntity testEntity);

    @InheritInverseConfiguration
    TestEntity toEntity(Test test);
//    @Mapping(source = "project", target = "projectEntity")
//    @Mapping(source = "page", target = "pageEntity")
//    TestEntity toEntity(Test test, Project project, Page page);
}
