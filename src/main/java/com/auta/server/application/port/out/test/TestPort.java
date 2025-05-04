package com.auta.server.application.port.out.test;

import com.auta.server.domain.test.Test;
import java.util.List;

public interface TestPort {
    List<Test> findAllByPageId(Long pageId);

    void deleteAllByPageId(Long id);

}
