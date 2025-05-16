package com.auta.server.adapter.out.persistence.project;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.project.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectQueryRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private ProjectQueryRepositoryImpl projectQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
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
                .userEntity(userEntity2)
                .figmaUrl("https://figma.com")
                .rootFigmaPage("mainPage")
                .serviceUrl("https://service.com")
                .projectName("캡스톤 설계")
                .description("프로젝트 설명입니다.")
                .projectCreatedDate(registeredDate3)
                .projectEnd(LocalDate.of(2025, 4, 4))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .build();

        projectRepository.saveAll(List.of(projectEntity1, projectEntity2, projectEntity3, projectEntity4));
        String email = userEntity1.getEmail();

        //when
        List<ProjectEntity> result = projectQueryRepository.findByProjectNameWithPaging(
                email, "캡스톤",
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