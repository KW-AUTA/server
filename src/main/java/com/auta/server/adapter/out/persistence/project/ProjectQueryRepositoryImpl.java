package com.auta.server.adapter.out.persistence.project;

import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

        BooleanExpression condition = (projectName == null || projectName.isBlank())
                ? null
                : projectEntity.projectName.containsIgnoreCase(projectName);

        OrderSpecifier<?> orderSpecifier = switch (sortBy) {
            case "createdDate" -> projectEntity.projectCreatedDate.desc();
            case "name" -> projectEntity.projectName.asc();
            default -> projectEntity.id.desc();
        };
        
        if (cursor != null) {
            ProjectEntity cursorEntity = queryFactory
                    .selectFrom(projectEntity)
                    .where(projectEntity.id.eq(cursor))
                    .fetchOne();

            if (cursorEntity == null) {
                throw new BusinessException(ErrorCode.INVALID_CURSOR);
            }

            BooleanExpression cursorCondition = switch (sortBy) {
                case "createdDate" -> projectEntity.projectCreatedDate.lt(cursorEntity.getProjectCreatedDate());
                case "name" -> projectEntity.projectName.gt(cursorEntity.getProjectName());
                default -> projectEntity.id.lt(cursorEntity.getId());
            };

            condition = (condition == null) ? cursorCondition : condition.and(cursorCondition);
        }

        return queryFactory
                .selectFrom(projectEntity)
                .where(condition)
                .orderBy(orderSpecifier)
                .limit(pageSize)
                .fetch();
    }
}
