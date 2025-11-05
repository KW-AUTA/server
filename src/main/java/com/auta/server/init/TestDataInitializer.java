package com.auta.server.init;

import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.page.PageRepository;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.test.TestEntity;
import com.auta.server.adapter.out.persistence.test.TestRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.project.ProjectStatus;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile({"local"})
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final TestRepository testRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. 사용자
        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@auta.com")
                .password(passwordEncoder.encode("test1234"))
                .username("테스트유저")
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .build());

        // 2. 프로젝트 2개
        ProjectEntity project1 = projectRepository.save(ProjectEntity.builder()
                .userEntity(user)
                .projectName("AUTA 테스트 프로젝트")
                .description("설명입니다")
                .figmaUrl("https://figma.com/proj1")
                .rootFigmaPage("홈페이지")
                .serviceUrl("https://auta.com")
                .projectCreatedDate(LocalDate.now())
                .projectEnd(LocalDate.now().plusDays(30))
                .projectStatus(ProjectStatus.COMPLETED)
                .testExecuteTime(LocalDateTime.now())
                .testRate(60)
                .build());

        ProjectEntity project2 = projectRepository.save(ProjectEntity.builder()
                .userEntity(user)
                .projectName("Jane의 프로젝트")
                .description("테스트용 프로젝트입니다")
                .figmaUrl("https://figma.com/proj2")
                .rootFigmaPage("대시보드")
                .serviceUrl("https://project2.com")
                .projectCreatedDate(LocalDate.now())
                .projectEnd(LocalDate.now().plusDays(15))
                .projectStatus(ProjectStatus.COMPLETED)
                .testExecuteTime(LocalDateTime.now())
                .testRate(40)
                .build());

        ProjectEntity project3 = projectRepository.save(ProjectEntity.builder()
                .userEntity(user)
                .projectName("광운대 홈페이지")
                .description("광운대 홈페이지 프로젝트입니다.")
                .figmaUrl("https://figma.com/proj2")
                .figmaJson("https://auta-json-s3.s3.ap-northeast-2.amazonaws.com/framesData+(6).json")
                .rootFigmaPage("home")
                .serviceUrl("https://www.kw.ac.kr/ko/index.jsp")
                .projectCreatedDate(LocalDate.now())
                .projectEnd(LocalDate.now().plusDays(15))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .testExecuteTime(LocalDateTime.now())
                .build());

        ProjectEntity project4 = projectRepository.save(ProjectEntity.builder()
                .userEntity(user)
                .projectName("test test")
                .description("test test")
                .figmaUrl(
                        "https://www.figma.com/design/3kN7qbAYdG9JTRK09G1lQK/Untitled?node-id=19-3&t=JRPrqp33fkv8ukh2-0")
                .figmaJson(
                        "https://auta-json-s3.s3.ap-northeast-2.amazonaws.com/b4e59a14-eafe-482d-be84-03f522ece2f3.json")
                .rootFigmaPage("thirdweb.studio")
                .serviceUrl("https://thirdweb.studio/")
                .projectCreatedDate(LocalDate.now())
                .projectEnd(LocalDate.now().plusDays(15))
                .projectStatus(ProjectStatus.NOT_STARTED)
                .testExecuteTime(LocalDateTime.now())
                .build());

        // 3. 페이지들
        PageEntity p1 = pageRepository.save(new PageEntity(null, project1, "홈", "/home"));
        PageEntity p2 = pageRepository.save(new PageEntity(null, project1, "회원가입", "/signup"));
        PageEntity p3 = pageRepository.save(new PageEntity(null, project1, "로그인", "/login"));
        PageEntity p4 = pageRepository.save(new PageEntity(null, project1, "대시보드", "/dashboard"));

        PageEntity p5 = pageRepository.save(new PageEntity(null, project2, "대시보드 홈", "/dashboard/home"));
        PageEntity p6 = pageRepository.save(new PageEntity(null, project2, "설정", "/settings"));
        PageEntity p7 = pageRepository.save(new PageEntity(null, project2, "통계", "/stats"));

        // 4. 테스트들
        testRepository.saveAll(List.of(
                new TestEntity(null, project1, p1, TestStatus.PASSED, TestType.ROUTING,
                        null, null, "#start-btn", "/signup", "/signup", "이동", "이동"),

                new TestEntity(null, project1, p1, TestStatus.PASSED, TestType.INTERACTION,
                        null, null, "#submit", null, null, "회원가입 제출", "서버 호출"),

                new TestEntity(null, project1, p1, TestStatus.FAILED, TestType.ROUTING,
                        "잘못된 경로로 이동", "세부 정보", "#login-btn", "/dashboard", "/error", "이동", "에러"),

                new TestEntity(null, project1, p2, TestStatus.PASSED, TestType.MAPPING,
                        null, "세부 정보", null, null, null, null, null),

                new TestEntity(null, project1, p3, TestStatus.FAILED, TestType.INTERACTION,
                        "서버 오류", "세부 정보", "#join", null, null, "회원가입 클릭", "500에러"),

                // 프로젝트 2
                new TestEntity(null, project2, p5, TestStatus.PASSED, TestType.ROUTING,
                        null, "세부 정보", "#go-stats", "/stats", "/stats", "이동", "이동"),

                new TestEntity(null, project2, p5, TestStatus.FAILED, TestType.ROUTING,
                        "404 Not Found", "세부 정보", "#invalid", "/unknown", "/404", "이동", "에러"),

                new TestEntity(null, project2, p6, TestStatus.PASSED, TestType.INTERACTION,
                        null, "세부 정보", "#save-btn", null, null, "설정 저장", "서버 호출"),

                new TestEntity(null, project2, p6, TestStatus.FAILED, TestType.MAPPING,
                        "컴포넌트 미노출", "세부 정보", null, null, null, null, null),

                new TestEntity(null, project2, p7, TestStatus.FAILED, TestType.ROUTING,
                        null, "세부 정보", "#home", "/dashboard/home", "/dashboard/home", "이동", "이동")
        ));
    }
}
