package CacheSmartCatalog.controller;

import CacheSmartCatalog.request.LoginRequest;
import CacheSmartCatalog.request.SignupRequest;
import CacheSmartCatalog.response.ApiResponse;
import CacheSmartCatalog.response.LoginResponse;
import CacheSmartCatalog.service.Auth.IAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest signupRequest) {

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .data(authService.signup(signupRequest))
                .message("signup success")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    private ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(loginRequest);
        Cookie cookie = authService.createCookie(loginResponse.getRefreshToken());

        response.addCookie(cookie);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .message("login success")
                .data(loginResponse)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping("/oauth-success")
    ResponseEntity<ApiResponse> oAuthSuccess(@RequestParam String token, HttpServletResponse response) {

        LoginResponse loginResponse = authService.oauthSuccess(token);
        Cookie cookie = authService.createCookie(loginResponse.getRefreshToken());
        response.addCookie(cookie);
        ApiResponse apiResponse = ApiResponse
                .builder()
                .success(true)
                .message("login success")
                .data(loginResponse)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

}
