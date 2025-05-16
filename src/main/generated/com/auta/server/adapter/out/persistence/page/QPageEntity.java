package com.auta.server.adapter.out.persistence.page;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPageEntity is a Querydsl query type for PageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPageEntity extends EntityPathBase<PageEntity> {

    private static final long serialVersionUID = 2030952952L;

    public static final QPageEntity pageEntity = new QPageEntity("pageEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QPageEntity(String variable) {
        super(PageEntity.class, forVariable(variable));
    }

    public QPageEntity(Path<? extends PageEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPageEntity(PathMetadata metadata) {
        super(PageEntity.class, metadata);
    }

}

