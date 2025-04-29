package com.auta.server.application.service.user;

import com.auta.server.application.port.in.user.UserCreateCommand;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import com.auta.server.application.port.in.user.UserUseCase;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(UserCreateCommand command) {
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        User user = User.builder()
                .email(command.getEmail())
                .password(encodedPassword)
                .username(command.getUsername())
                .build();
        return userPort.save(user);
    }

    @Override
    public User getUser(String email) {
        Optional<User> optionalUser = userPort.findByEmail(email);

        return optionalUser
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateCommand command, String email) {
        Optional<User> optionalUser = userPort.findByEmail(email);
        User user = optionalUser
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.update(
                command.getEmail(),
                command.getUsername(),
                command.getPassword(),
                command.getAddress(),
                command.getPhoneNumber()
        );

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userPort.deleteByEmail(email);
    }
}
