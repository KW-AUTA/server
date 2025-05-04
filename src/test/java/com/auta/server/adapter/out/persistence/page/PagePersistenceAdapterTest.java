package com.auta.server.adapter.out.persistence.page;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.page.Page;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PagePersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    private PagePersistenceAdapter pagePersistenceAdapter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PageRepository pageRepository;

    @AfterEach
    void tearDown() {
        pageRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("페이지의 아이디를 입력받아. 페이지를 반환한다.")
    @Test
    void findById() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());
        ProjectEntity projectEntity = projectRepository.save(createDummyProject(userEntity));
        PageEntity pageEntity = pageRepository.save(createDummyPage(projectEntity));

        PageEntity saved = pageRepository.save(pageEntity);
        Long pageId = saved.getId();

        //when
        Optional<Page> optionalPage = pagePersistenceAdapter.findById(pageId);

        //then
        assertThat(optionalPage).isPresent();
    }

    @DisplayName("프로젝트 아이디로 페이지 리스트를 불러온다.")
    @Test
    void findAllByProjectId() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());
        ProjectEntity projectEntity = projectRepository.save(createDummyProject(userEntity));
        PageEntity pageEntity = pageRepository.save(createDummyPage(projectEntity));

        PageEntity saved = pageRepository.save(pageEntity);
        Long projectId = pageEntity.getId();

        //when
        List<Page> pages = pagePersistenceAdapter.findAllIdsByProjectId(projectId);

        //then
        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).getProject())
                .extracting("id").isEqualTo(1L);
    }

    private PageEntity createDummyPage(ProjectEntity projectEntity) {
        return PageEntity.builder()
                .projectEntity(projectEntity)
                .build();
    }

    private ProjectEntity createDummyProject(UserEntity userEntity) {
        return ProjectEntity.builder()
                .userEntity(userEntity)
                .build();
    }

    private UserEntity createDummyUser() {
        return UserEntity.builder()
                .build();
    }
}