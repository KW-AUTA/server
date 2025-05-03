package com.auta.server.adapter.out.persistence.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectEntity is a Querydsl query type for ProjectEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectEntity extends EntityPathBase<ProjectEntity> {

    private static final long serialVersionUID = 2106468014L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectEntity projectEntity = new QProjectEntity("projectEntity");

    public final StringPath description = createString("description");

    public final StringPath figmaUrl = createString("figmaUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> projectCreatedDate = createDate("projectCreatedDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> projectEnd = createDate("projectEnd", java.time.LocalDate.class);

    public final StringPath projectName = createString("projectName");

    public final EnumPath<com.auta.server.domain.project.ProjectStatus> projectStatus = createEnum("projectStatus", com.auta.server.domain.project.ProjectStatus.class);

    public final StringPath rootFigmaPage = createString("rootFigmaPage");

    public final StringPath serviceUrl = createString("serviceUrl");

    public final NumberPath<Integer> testExecuteTime = createNumber("testExecuteTime", Integer.class);

    public final NumberPath<Integer> testRate = createNumber("testRate", Integer.class);

    public final com.auta.server.adapter.out.persistence.user.QUserEntity user;

    public QProjectEntity(String variable) {
        this(ProjectEntity.class, forVariable(variable), INITS);
    }

    public QProjectEntity(Path<? extends ProjectEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectEntity(PathMetadata metadata, PathInits inits) {
        this(ProjectEntity.class, metadata, inits);
    }

    public QProjectEntity(Class<? extends ProjectEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.auta.server.adapter.out.persistence.user.QUserEntity(forProperty("user")) : null;
    }

}

