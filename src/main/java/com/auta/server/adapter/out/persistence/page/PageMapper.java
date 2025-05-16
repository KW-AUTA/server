package com.auta.server.adapter.out.persistence.page;

import com.auta.server.adapter.out.persistence.project.ProjectMapper;
import com.auta.server.domain.page.Page;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ProjectMapper.class})
public interface PageMapper {

    @Mapping(source = "projectEntity", target = "project")
    @Mapping(target = "tests", ignore = true)
    Page toDomain(PageEntity pageEntity);

    @InheritInverseConfiguration
    PageEntity toEntity(Page page);
}
