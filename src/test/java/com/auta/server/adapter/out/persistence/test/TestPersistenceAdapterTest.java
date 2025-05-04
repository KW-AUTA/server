package com.auta.server.adapter.out.persistence.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.page.PageRepository;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.adapter.out.persistence.project.ProjectRepository;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class TestPersistenceAdapterTest extends IntegrationTestSupport {
    @Autowired
    private TestPersistenceAdapter testPersistenceAdapter;

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

    @DisplayName("페이지 아이디로 테스트 리스트를 조회한다.")
    @Test
    void findAllByPageId() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());
        ProjectEntity projectEntity = projectRepository.save(createDummyProject(userEntity));
        PageEntity pageEntity = pageRepository.save(createDummyPage(projectEntity));

        List<TestEntity> testEntities = List.of(
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.INTERACTION),
                createDummyTest(projectEntity, pageEntity, TestStatus.READY, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.COMPONENT)
        );

        List<TestEntity> savedTests = testRepository.saveAll(testEntities);
        Long pageId = pageEntity.getId();

        //when
        List<com.auta.server.domain.test.Test> tests = testPersistenceAdapter.findAllByPageId(pageId);

        //then
        assertThat(tests).hasSize(5);

    }

    @Transactional
    @DisplayName("페이지 아이디로 테스트 리스트를 삭제한다.")
    @Test
    void deleteAllByPageId() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());
        ProjectEntity projectEntity = projectRepository.save(createDummyProject(userEntity));
        PageEntity pageEntity = pageRepository.save(createDummyPage(projectEntity));

        List<TestEntity> testEntities = List.of(
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.PASSED, TestType.INTERACTION),
                createDummyTest(projectEntity, pageEntity, TestStatus.READY, TestType.ROUTING),
                createDummyTest(projectEntity, pageEntity, TestStatus.FAILED, TestType.COMPONENT)
        );

        List<TestEntity> savedTests = testRepository.saveAll(testEntities);
        Long pageId = pageEntity.getId();

        //when
        testPersistenceAdapter.deleteAllByPageId(pageId);

        //then
        assertThat(testRepository.findAllByPageId(pageId)).isEmpty();
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
                .userEntity(userEntity)
                .build();
    }

    private UserEntity createDummyUser() {
        return UserEntity.builder()
                .build();
    }
}