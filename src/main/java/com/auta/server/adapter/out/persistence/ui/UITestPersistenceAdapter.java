package com.auta.server.adapter.out.persistence.ui;

import com.auta.server.application.port.out.persistence.ui.UITestPort;
import com.auta.server.domain.ui.UITest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UITestPersistenceAdapter implements UITestPort {
    private final UITestRepository uiTestRepository;
    private final UITestMapper uiTestMapper;

    @Override
    public List<UITest> saveAll(List<UITest> uiTests) {
        List<UITestEntity> uiTestEntities = uiTestRepository.saveAll(
                uiTests.stream().map(uiTestMapper::toEntity).toList());
        return uiTestEntities.stream().map(uiTestMapper::toDomain).toList();
    }

    @Override
    public List<UITest> findAllByProjectId(Long projectId) {
        List<UITestEntity> uiTestEntities = uiTestRepository.findAllByProjectId(projectId);
        return uiTestEntities.stream().map(uiTestMapper::toDomain).toList();
    }
}
