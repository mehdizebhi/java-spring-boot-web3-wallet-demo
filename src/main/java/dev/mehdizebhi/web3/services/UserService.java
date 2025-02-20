package dev.mehdizebhi.web3.services;

import dev.mehdizebhi.web3.entities.UserEntity;
import dev.mehdizebhi.web3.entities.UserSession;
import dev.mehdizebhi.web3.exceptions.NotFoundEntity;
import dev.mehdizebhi.web3.exceptions.UserExistsException;
import dev.mehdizebhi.web3.models.LoginResult;
import dev.mehdizebhi.web3.models.SignupResult;
import dev.mehdizebhi.web3.models.SpringUser;
import dev.mehdizebhi.web3.repositories.UserRepository;
import dev.mehdizebhi.web3.repositories.UserSessionRepository;
import dev.mehdizebhi.web3.utils.JwtUtils;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResult login(String username, String password) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            if (!passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
                throw new BadCredentialsException("Login incorrect");
            }
            var springUser = new SpringUser(userOpt.get());
            return new LoginResult(loginToken(springUser), springUser);
        }
        throw new NotFoundEntity();
    }

    public SignupResult signup(@Email String email, String username, String password) {
        boolean isUsernameOrEmailAlreadyExist = userRepository.existsByUsernameOrEmail(username, email);
        if (isUsernameOrEmailAlreadyExist) {
            throw new UserExistsException();
        }
        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPasswordHash(passwordEncoder.encode(password));
        userEntity = userRepository.save(userEntity);
        var springUser = new SpringUser(userEntity);
        return new SignupResult(loginToken(springUser), springUser);
    }

    public UserEntity findByToken(String token) {
        return userRepository.findByToken(token, Instant.now()).orElseThrow(() -> new NotFoundEntity());
    }

    public void logout(String token) {
    }

    // ---------------------------------
    // Private Helper
    // ---------------------------------

    private String loginToken(SpringUser springUser) {
        new AccountStatusUserDetailsChecker().check(springUser);
        var token = jwtUtils.generateToken(springUser);
        var session = UserSession.builder()
                .user(springUser.getUser())
                .token(token)
                .tokenIssueAt(Instant.now())
                .tokenExpireAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        userSessionRepository.save(session);
        return token;
    }

}