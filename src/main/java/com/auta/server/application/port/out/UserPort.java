package com.auta.server.application.port.out;

import com.auta.server.domain.user.User;

public interface UserPort {
    User save(User user);
}
