package com.auta.server.application.service.project;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.application.port.out.project.ProjectSummaryQueryDto;
import com.auta.server.domain.project.ProjectStatus;
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
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
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
}