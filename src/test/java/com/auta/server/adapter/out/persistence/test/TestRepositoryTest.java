package com.auta.server.adapter.out.persistence.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.page.PageRepository;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private TestRepository testRepository;

    @AfterEach
    void tearDown() {
        testRepository.deleteAllInBatch();
        pageRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("프로젝트 이름 리스트를 입력하면 해당하는 테스트 리스트가 반환된다. 이때 생성일 기준으로 내림차순 정렬이다.")
    @Test
    void findAllByProjectIdInOrderByCreatedTime() {
        UserEntity u1 = userRepository.save(createDummyUser());

        ProjectEntity p1 = projectRepository.save(createDummyProject(u1));
        ProjectEntity p2 = projectRepository.save(createDummyProject(u1));

        PageEntity pa1 = pageRepository.save(createDummyPage(p1));
        PageEntity pa2 = pageRepository.save(createDummyPage(p2));

        TestEntity t1 = testRepository.save(createDummyTest(p1, pa1));
        TestEntity t2 = testRepository.save(createDummyTest(p1, pa1));
        TestEntity t3 = testRepository.save(createDummyTest(p2, pa2));

        testRepository.save(t1);
        testRepository.save(t2);
        testRepository.save(t3);

        // when
        List<TestEntity> result = testRepository.findAllByProjectIdInOrderByCreatedTime(
                List.of(p1.getId(), p2.getId()));

        // then
        assertThat(result).hasSize(3);
//        assertThat(result).isSortedAccordingTo(
//                Comparator.comparing(TestEntity::getCreatedDateTime)
//        );
        assertThat(result).isSortedAccordingTo((a, b) -> b.getCreatedDateTime().compareTo(a.getCreatedDateTime()));
    }

    private TestEntity createDummyTest(ProjectEntity projectEntity, PageEntity pageEntity) {
        return TestEntity.builder()
                .projectEntity(projectEntity)
                .pageEntity(pageEntity)
                .build();
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