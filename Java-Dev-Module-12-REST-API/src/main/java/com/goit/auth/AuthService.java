package com.goit.auth;

import com.goit.auth.dto.login.LoginRequest;
import com.goit.auth.dto.registration.RegistrationRequest;
import com.goit.generic_response.Response;
import com.goit.security.JWTUtils;
import com.goit.users.User;
import com.goit.users.UserRepository;
import com.goit.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int MAX_USER_ID_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 255;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_AGE = 100;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtil;
    private final UserRepository repository;

    public Response<Void> register(RegistrationRequest request) {
        User existingUser = userService.findByUsername(request.getEmail());
        if (Objects.nonNull(existingUser)) {
            return Response.failed(Response.Error.USER_ALREADY_EXISTS);
        }

        Optional<Response.Error> validationError = validateRegistrationFields(request);
        if (validationError.isPresent()) {
            return Response.failed(validationError.get());
        }

        repository.save(User.builder()
                .userId(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .age(request.getAge())
                .build());

        return Response.success(null);
    }

    public Response<String> login(LoginRequest request) {
        Optional<Response.Error> validationError = validateLoginFields(request);
        if (validationError.isPresent()) {
            return Response.failed(validationError.get());
        }

        User user = userService.findByUsername(request.getEmail());

        if (Objects.isNull(user)) {
            return Response.failed(Response.Error.INVALID_EMAIL);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return Response.failed(Response.Error.INVALID_PASSWORD);
        }

        String authToken = jwtUtil.generateToken(request.getEmail());
        return Response.success(authToken);
    }

    private Optional<Response.Error> validateRegistrationFields(RegistrationRequest request) {
        if (Objects.isNull(request.getEmail()) || request.getEmail().length() > MAX_USER_ID_LENGTH) {
            return Optional.of(Response.Error.INVALID_EMAIL);
        }
        if (Objects.isNull(request.getPassword()) || request.getPassword().length() > MAX_PASSWORD_LENGTH) {
            return Optional.of(Response.Error.INVALID_PASSWORD);
        }
        if (Objects.isNull(request.getName()) || request.getName().length() > MAX_NAME_LENGTH) {
            return Optional.of(Response.Error.INVALID_NAME);
        }
        if (request.getAge() > MAX_AGE) {
            return Optional.of(Response.Error.INVALID_AGE);
        }
        return Optional.empty();
    }

    private Optional<Response.Error> validateLoginFields(LoginRequest request) {
        if (Objects.isNull(request.getEmail()) || request.getEmail().length() > MAX_USER_ID_LENGTH) {
            return Optional.of(Response.Error.INVALID_EMAIL);
        }
        if (Objects.isNull(request.getPassword()) || request.getPassword().length() > MAX_PASSWORD_LENGTH) {
            return Optional.of(Response.Error.INVALID_PASSWORD);
        }
        return Optional.empty();
    }
}