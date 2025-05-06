package com.auta.server.application.service.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.page.PageRepository;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.test.TestEntity;
import com.auta.server.adapter.out.persistence.test.TestRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.application.port.in.project.ProjectDetailDto;
import com.auta.server.application.port.in.project.ProjectTestSummaryDto;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectQueryServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private ProjectQueryServiceImpl projectQueryService;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        testRepository.deleteAllInBatch();
        pageRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("프로젝트 요약 리스트를 불러온다. 이 때 프로젝트 이름과 정렬 방식 커서위치 등을 입력 받고 이를 바탕으로 리스트를 반환한다.")
    @Test
    void getProjectSummaryList() {
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
                .userEntity(userEntity1)
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
                .userEntity(userEntity1)
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
                .userEntity(userEntity1)
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
                .userEntity(userEntity1)
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
        List<ProjectSummaryQueryDto> result = projectQueryService.getProjectSummaryList(
                "캡스톤",
                "createdDate",
                (Long) null
        );

        //then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getProjectName()).isEqualTo("캡스톤 설계");
        assertThat(result.get(1).getProjectName()).isEqualTo("캡스톤 리뷰");
        assertThat(result.get(2).getProjectName()).isEqualTo("캡스톤 프로젝트");
    }

    @DisplayName("프로젝트 상세 정보를 불러온다.")
    @Test
    void getProjectDetail() {
        //given
        LocalDate registeredDate1 = LocalDate.now();
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2
        ));
        ProjectEntity projectEntity1 = ProjectEntity.builder()
                .userEntity(userEntity1)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("캡스톤 프로젝트")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate1)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();

        projectRepository.saveAll(List.of(projectEntity1));
        Long projectId = projectEntity1.getId();

        PageEntity pageEntity = PageEntity.builder()
                .pageName("Home")
                .pageBaseUrl("/home")
                .projectEntity(projectEntity1)
                .build();

        pageEntity = pageRepository.save(pageEntity);

        TestEntity testEntity = TestEntity.builder()
                .projectEntity(projectEntity1)
                .testType(TestType.ROUTING)
                .pageEntity(pageEntity)
                .build();

        testRepository.save(testEntity);

        //when
        ProjectDetailDto projectDetail = projectQueryService.getProjectDetail(projectId);

        //then
        assertThat(projectDetail.getProject()).isNotNull();
        projectDetail.getPages().forEach(page ->
                assertThat(page.getPageName()).isNotNull()
        );
    }

    @DisplayName("프로젝트 테스트 요약 리스트를 불러온다. 이 때 프로젝트 이름과 정렬 방식 커서 위치 등을 입력 받고 바탕으로 해당 프로젝튿르의 테스트 요약 리스트를 반환한다.")
    @Test
    void getProjectTestSummaryList() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());
        ProjectEntity projectEntity = projectRepository.save(createDummyProject(userEntity));
        PageEntity pageEntity = pageRepository.save(createDummyPage(projectEntity));

        List<TestEntity> testEntities = List.of(
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.INTERACTION),
                createDummyTest(projectEntity, pageEntity, TestStatus.READY, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.MAPPING)
        );

        List<TestEntity> savedTests = testRepository.saveAll(testEntities);

        //when
        List<ProjectTestSummaryDto> projectTestSummaryList = projectQueryService.getProjectTestSummaryList(
                "캡스톤",
                "",
                (Long) null
        );

        //then
        assertThat(projectTestSummaryList).extracting("totalRoutingTest", "totalInteractionTest", "totalMappingTest",
                        "successRoutingTest", "successInteractionTest", "successMappingTest")
                .containsExactlyInAnyOrder(tuple(3, 1, 1, 1, 1, 0));

    }

    private TestEntity createDummyTest(ProjectEntity projectEntity, PageEntity pageEntity, TestStatus testStatus,
                                       TestType testType) {
        return TestEntity.builder()
                .projectEntity(projectEntity)
                .pageEntity(pageEntity)
                .testStatus(testStatus)
                .testType(testType)
                .build();
    }

    private PageEntity createDummyPage(ProjectEntity projectEntity) {
        return PageEntity.builder()
                .projectEntity(projectEntity)
                .build();
    }

    private ProjectEntity createDummyProject(UserEntity userEntity) {
        return ProjectEntity.builder()
                .projectName("캡스톤")
                .projectCreatedDate(LocalDate.now())
                .userEntity(userEntity)
                .build();
    }

    private UserEntity createDummyUser() {
        return UserEntity.builder()
                .build();
    }
}