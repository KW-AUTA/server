package com.auta.server.adapter.out.persistence.test;

import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.domain.test.Test;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestPersistenceAdapter implements TestPort {
    private final TestRepository testRepository;
    private final TestMapper testMapper;

    @Override
    public List<Test> findAllByPageId(Long pageId) {
        List<TestEntity> testEntities = testRepository.findAllByPageId(pageId);
        return testEntities.stream().map(testMapper::toDomain).toList();
    }

    @Override
    public List<Test> findAllByProjectId(Long projectId) {
        List<TestEntity> testEntities = testRepository.findAllByProjectId(projectId);
        return testEntities.stream().map(testMapper::toDomain).toList();
    }

    @Override
    public List<Test> findAllByProjectIdInOrderByCreationTimeDesc(List<Long> projectIds) {
        List<TestEntity> testEntities = testRepository.findAllByProjectIdInOrderByCreatedTime(projectIds);
        return testEntities.stream().map(testMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteAllByProjectId(Long projectId) {
        testRepository.deleteAllByProjectId(projectId);
    }

    @Override
    public List<Test> saveAll(List<Test> tests) {
        List<TestEntity> testEntities = testRepository.saveAll(tests.stream().map(testMapper::toEntity).toList());
        return testEntities.stream().map(testMapper::toDomain).toList();
    }

    @Override
    public Test save(Test test) {
        TestEntity testEntity = testRepository.save(testMapper.toEntity(test));
        return testMapper.toDomain(testEntity);
    }
}
