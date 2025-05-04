package com.auta.server.application.port.in.page;

import com.auta.server.domain.test.Test;
import java.util.List;

public interface PageUseCase {
    List<Test> getPageTestDetail(Long pageId);
}
