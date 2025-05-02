package com.auta.server.adapter.out.persistence.project;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserMapper;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectPersistenceAdapterTest extends IntegrationTestSupport {
    @Autowired
    private ProjectPersistenceAdapter projectPersistenceAdapter;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("프로젝트를 저장한다.")
    @Test
    void save() {
        //given
        LocalDate registeredDate = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));

        User user = UserMapper.toDomain(userEntity1);

        Project project = Project.builder()
                .user(user)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();

        //when
        Project savedProject = projectPersistenceAdapter.save(project);

        //then
        assertThat(savedProject).extracting("projectCreatedDate", "projectStatus")
                .contains(registeredDate, ProjectStatus.NOT_STARTED);
        assertThat(savedProject.getUser().getUsername()).isEqualTo("testUser1");
    }
}