package com.auta.server.adapter.out.persistence.project;

import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepositoryImpl implements ProjectQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectEntity> findByProjectNameWithPaging(String projectName, String sortBy, Long cursor,
                                                           int pageSize) {
        QProjectEntity projectEntity = QProjectEntity.projectEntity;

        JPAQuery<ProjectEntity> baseQuery = queryFactory
                .selectFrom(projectEntity)
                .where(projectEntity.projectName.containsIgnoreCase(projectName));

        if (cursor != null) {
            ProjectEntity cursorEntity = queryFactory
                    .selectFrom(projectEntity)
                    .where(projectEntity.id.eq(cursor))
                    .fetchOne();

            if (cursorEntity == null) {
                throw new BusinessException(ErrorCode.INVALID_CURSOR);
            }

            switch (sortBy) {
                case "createdDate" -> {
                    baseQuery.where(projectEntity.projectCreatedDate.lt(cursorEntity.getProjectCreatedDate()));
                }
                case "name" -> baseQuery.where(projectEntity.projectName.gt(cursorEntity.getProjectName()));
                default -> baseQuery.where(projectEntity.id.lt(cursorEntity.getId()));
            }
        }

        OrderSpecifier<?> orderSpecifier = switch (sortBy) {
            case "createdDate" -> projectEntity.projectCreatedDate.desc();
            case "name" -> projectEntity.projectName.asc();
            default -> projectEntity.id.desc();
        };

        return baseQuery
                .orderBy(orderSpecifier)
                .limit(pageSize)
                .fetch();
    }
}
