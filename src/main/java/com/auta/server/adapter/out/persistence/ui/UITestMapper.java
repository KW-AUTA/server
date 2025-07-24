package com.auta.server.adapter.out.persistence.ui;

import com.auta.server.adapter.out.persistence.project.ProjectMapper;
import com.auta.server.domain.ui.UITest;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface UITestMapper {
    @Mapping(source = "projectEntity", target = "project")
    UITest toDomain(UITestEntity uiTestEntity);

    @InheritInverseConfiguration
    UITestEntity toEntity(UITest uiTest);
}
