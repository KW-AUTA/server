package com.auta.server.application.port.out.page;

import com.auta.server.domain.page.Page;
import java.util.Optional;

public interface PagePort {
    Optional<Page> findById(Long pageId);
}
