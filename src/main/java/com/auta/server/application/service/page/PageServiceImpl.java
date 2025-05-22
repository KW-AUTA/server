package com.auta.server.application.service.page;

import com.auta.server.application.port.in.page.PageUseCase;
import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.test.Test;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl implements PageUseCase {
    private final PagePort pagePort;
    private final TestPort testPort;

    @Override
    public List<Test> getPageTestDetail(Long pageId) {
        Page page = pagePort.findById(pageId).orElseThrow(() -> new BusinessException(ErrorCode.PAGE_NOT_FOUND));
        return testPort.findAllByPageId(pageId);
    }
}
