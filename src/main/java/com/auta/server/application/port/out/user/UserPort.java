package com.auta.server.application.port.out.user;

import com.auta.server.domain.user.User;
import java.util.Optional;

public interface UserPort {
    User save(User user);

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    User update(User user);
}
