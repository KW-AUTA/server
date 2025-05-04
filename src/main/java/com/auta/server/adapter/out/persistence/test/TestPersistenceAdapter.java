package com.auta.server.adapter.out.persistence.test;

import com.auta.server.application.port.out.test.TestPort;
import com.auta.server.domain.test.Test;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public void deleteAllByPageId(Long pageId) {
        testRepository.deleteAllByPageId(pageId);
    }
}
