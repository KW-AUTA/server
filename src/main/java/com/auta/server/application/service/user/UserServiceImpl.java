package com.auta.server.application.service.user;

import com.auta.server.application.port.in.project.ProjectUseCase;
import com.auta.server.application.port.in.user.UserCreateCommand;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import com.auta.server.application.port.in.user.UserUseCase;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import java.util.List;
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
    private final ProjectUseCase projectUseCase;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(UserCreateCommand command) {
        String encodedPassword = encodePassWord(command);
        User user = generateUser(command, encodedPassword);
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
        User user = findUserBy(email);

        updateUser(command, user);

        return userPort.update(user);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        User user = userPort.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<Project> projects = projectUseCase.findAllByUserId(user.getId());
        projects.forEach(project -> projectUseCase.deleteProject(project.getId()));

        userPort.deleteByEmail(email);
    }

    private String encodePassWord(UserCreateCommand command) {
        return passwordEncoder.encode(command.getPassword());
    }

    private User generateUser(UserCreateCommand command, String encodedPassword) {
        return User.builder()
                .email(command.getEmail())
                .password(encodedPassword)
                .username(command.getUsername())
                .build();
    }

    private User findUserBy(String email) {
        Optional<User> optionalUser = userPort.findByEmail(email);
        return optionalUser
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private void updateUser(UserUpdateCommand command, User user) {
        user.update(
                command.getEmail(),
                command.getUsername(),
                command.getPassword(),
                command.getAddress(),
                command.getPhoneNumber()
        );
    }
}
