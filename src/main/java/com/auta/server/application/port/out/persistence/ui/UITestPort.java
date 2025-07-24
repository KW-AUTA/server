package com.auta.server.application.port.out.persistence.ui;

import com.auta.server.domain.ui.UITest;
import java.util.List;

public interface UITestPort {
    List<UITest> saveAll(List<UITest> uiTests);

    List<UITest> findAllByProjectId(Long projectId);
}
