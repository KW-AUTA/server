package com.auta.server.adapter.out.persistence.page;

import com.auta.server.application.port.out.page.PagePort;
import com.auta.server.domain.page.Page;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagePersistenceAdapter implements PagePort {
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;

    @Override
    public Optional<Page> findById(Long pageId) {
        return pageRepository.findById(pageId).map(pageMapper::toDomain);
    }

    @Override
    public List<Page> findAllByProjectId(Long projectId) {
        return pageRepository.findAllByProjectId(projectId)
                .stream().map(pageMapper::toDomain).toList();
    }
}
