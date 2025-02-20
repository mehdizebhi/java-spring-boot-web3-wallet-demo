package dev.mehdizebhi.web3.controllers;

import dev.mehdizebhi.web3.controllers.dto.AuthTokenDto;
import dev.mehdizebhi.web3.controllers.dto.UserDto;
import dev.mehdizebhi.web3.controllers.request.LoginRequest;
import dev.mehdizebhi.web3.controllers.request.RegisterRequest;
import dev.mehdizebhi.web3.controllers.response.ApiResponse;
import dev.mehdizebhi.web3.exceptions.PasswordNotMatchException;
import dev.mehdizebhi.web3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final UserService userService;
    private static final long AUTH_TOKEN_EXPIRES_IN = 24 * 60 * 60 * 1000L;

    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse<AuthTokenDto>> register(@RequestBody RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordNotMatchException();
        }
        var result = userService.signup(request.email(), request.username(), request.password());
        return ResponseEntity.ok(ApiResponse.<AuthTokenDto>builder()
                .data(new AuthTokenDto(result.token(), "Bearer", AUTH_TOKEN_EXPIRES_IN))
                .message("success")
                .build());
    }

    @PostMapping("/login")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse<AuthTokenDto>> login(@RequestBody LoginRequest request) {
        var result = userService.login(request.username(), request.password());
        return ResponseEntity.ok(ApiResponse.<AuthTokenDto>builder()
                .data(new AuthTokenDto(result.token(), "Bearer", AUTH_TOKEN_EXPIRES_IN))
                .message("success")
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> me() {
        var user = user();
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .data(new UserDto(user.getUsername(), user.getEmail()))
                .message("success")
                .build());
    }
}