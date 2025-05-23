package com.auta.server.adapter.out.persistence.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
