package com.auta.server.application.service.project;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.page.PageRepository;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.test.TestRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectServiceImplTest extends IntegrationTestSupport {
    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private TestRepository testRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("프로젝트를 저장한다. 이때 프로젝트의 user는 로그인 유저이고 생성 날짜는 입력받은 날짜이다.")
    @Test
    void saveProject() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));
        String email = "test@example.com1";
        LocalDate registeredDate = LocalDate.now();
        ProjectCommand command = ProjectCommand.builder()
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트 설명입니다.")
                .projectEnd(LocalDate.of(2025, 4, 4))
                .build();

        //when
        Project project = projectService.createProject(command, email, registeredDate);

        //then
        assertThat(project).extracting("projectCreatedDate", "projectStatus")
                .contains(registeredDate, ProjectStatus.NOT_STARTED);
        assertThat(project.getUser().getUsername()).isEqualTo("testUser1");
    }

    @DisplayName("프로젝트를 수정한다.")
    @Test
    void updateProject() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));

        LocalDate registeredDate = LocalDate.now();

        ProjectEntity projectEntity = ProjectEntity.builder()
                .userEntity(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .testExecuteTime(0)
                .build();

        ProjectEntity saved = projectRepository.save(projectEntity);

        ProjectCommand command = ProjectCommand.builder()
                .figmaUrl("12")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트")
                .projectEnd(LocalDate.of(2025, 4, 4))
                .build();

        Long projectId = saved.getId();
        //when
        Project project = projectService.updateProject(command, projectId);

        //then
        assertThat(project).extracting("figmaUrl", "description")
                .contains("12", "프로젝트");
    }

    @DisplayName("프로젝트 아이디를 입력 받아서 프로젝트를 삭제한다.")
    @Test
    void deleteProject() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));

        LocalDate registeredDate = LocalDate.now();

        ProjectEntity projectEntity = ProjectEntity.builder()
                .userEntity(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();
        ProjectEntity saved = projectRepository.save(projectEntity);

        Long projectId = saved.getId();

        //when
        projectService.deleteProject(projectId);

        //then
        Optional<ProjectEntity> optionalProject = projectRepository.findById(projectId);

        assertThat(optionalProject).isEmpty();
    }
}