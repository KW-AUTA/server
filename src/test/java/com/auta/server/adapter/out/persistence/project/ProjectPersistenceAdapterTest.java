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
import java.util.Optional;
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

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private UserMapper userMapper;
    
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
        userRepository.saveAll(List.of(
                userEntity1
        ));

        User user = userMapper.toDomain(userEntity1);

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

    @DisplayName("프로젝트를 조회한다.")
    @Test
    void find() {
        //given
        LocalDate registeredDate = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        userRepository.saveAll(List.of(
                userEntity1
        ));

        ProjectEntity projectEntity = ProjectEntity.builder()
                .user(userEntity1)
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
        Optional<Project> project = projectPersistenceAdapter.findById(projectId);

        //then
        assertThat(project).isPresent();
        assertThat(project.get()).extracting("projectCreatedDate", "projectStatus")
                .contains(registeredDate, ProjectStatus.NOT_STARTED);
        assertThat(project.get().getUser().getUsername()).isEqualTo("testUser1");
    }

    @DisplayName("프로젝트를 조회한다. 프로젝트가 없으면 빈 optional을 반환한다.")
    @Test
    void findEmpty() {
        //given
        LocalDate registeredDate = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));

        ProjectEntity projectEntity = ProjectEntity.builder()
                .user(userEntity1)
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
        Long projectId = -1L;
        //when
        Optional<Project> project = projectPersistenceAdapter.findById(projectId);

        //then
        assertThat(project).isEmpty();
    }

    @DisplayName("프로젝트를 수정한다.")
    @Test
    void update() {
        //given
        LocalDate registeredDate = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        userRepository.saveAll(List.of(
                userEntity1
        ));
        ProjectEntity projectEntity = ProjectEntity.builder()
                .user(userEntity1)
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

        Project project = Project.builder()
                .id(saved.getId())
                .figmaUrl("12")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("UI 자동화 테스트")
                .description("프로젝트")
                .projectEnd(LocalDate.of(2025, 4, 4))
                .build();

        //when
        Project savedProject = projectPersistenceAdapter.update(project);

        //then
        assertThat(savedProject).extracting("figmaUrl", "description")
                .contains("12", "프로젝트");
    }

    @DisplayName("프로젝트를 삭제한다.")
    @Test
    void delete() {
        //given
        LocalDate registeredDate = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        userRepository.saveAll(List.of(
                userEntity1
        ));

        ProjectEntity projectEntity = ProjectEntity.builder()
                .user(userEntity1)
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

        Project project = projectMapper.toDomain(saved);

        //when
        projectPersistenceAdapter.delete(project);

        //then
        Optional<ProjectEntity> optionalProject = projectRepository.findById(project.getId());
        assertThat(optionalProject).isEmpty();
    }

    @DisplayName("프로젝트 이름, 정렬기준에 따른 커서기반 무한 스크롤 페이지를 반환한다.")
    @Test
    void findByProjectNameWithPaging() {
        //given
        LocalDate registeredDate1 = LocalDate.now();
        LocalDate registeredDate2 = LocalDate.now().plusDays(1);
        LocalDate registeredDate3 = LocalDate.now().plusDays(2);
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));
        ProjectEntity projectEntity1 = ProjectEntity.builder()
                .user(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("캡스톤 프로젝트")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate1)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();
        ProjectEntity projectEntity2 = ProjectEntity.builder()
                .user(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("캡스톤 리뷰")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate2)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();
        ProjectEntity projectEntity3 = ProjectEntity.builder()
                .user(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("캡스톤 설계")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate3)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();
        ProjectEntity projectEntity4 = ProjectEntity.builder()
                .user(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("광운대학교 설계")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate3)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();

        projectRepository.saveAll(List.of(projectEntity1, projectEntity2, projectEntity3, projectEntity4));

        //when
        List<Project> result = projectPersistenceAdapter.findByProjectNameWithPaging(
                "캡스톤",
                "createdDate",
                (Long) null,
                10
        );

        //then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getProjectName()).isEqualTo("캡스톤 설계");
        assertThat(result.get(1).getProjectName()).isEqualTo("캡스톤 리뷰");
        assertThat(result.get(2).getProjectName()).isEqualTo("캡스톤 프로젝트");
    }
}