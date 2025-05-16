package com.auta.server.adapter.out.persistence.project;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저 아이디로 프로젝트를 찾는다")
    @Test
    void findAllByUserId() {
        //given
        UserEntity userEntity = userRepository.save(createDummyUser());

        List<ProjectEntity> projectEntities = List.of(
                createDummyProject(userEntity),
                createDummyProject(userEntity),
                createDummyProject(userEntity),
                createDummyProject(userEntity));

        projectRepository.saveAll(projectEntities);
        Long userId = userEntity.getId();

        //when
        List<ProjectEntity> allByUserId = projectRepository.findAllByUserId(userId);

        //then
        assertThat(allByUserId).hasSize(4);
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