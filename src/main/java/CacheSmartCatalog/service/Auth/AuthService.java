package CacheSmartCatalog.service.Auth;

import CacheSmartCatalog.model.User;
import CacheSmartCatalog.request.LoginRequest;
import CacheSmartCatalog.request.SignupRequest;
import CacheSmartCatalog.response.LoginResponse;
import CacheSmartCatalog.response.SignupResponse;
import CacheSmartCatalog.service.JWT.JwtService;
import CacheSmartCatalog.service.User.IUserService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final JwtService jwtService;
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SignupResponse signup(SignupRequest signupRequest) {

        boolean isPresent = userService.getUserByUsername(signupRequest.getUsername()).isPresent();
        if (isPresent) {
            throw new BadCredentialsException("user already exist");
        }

        User savedUser = saveUser(signupRequest);

        SignupResponse response = convertToSignup(savedUser);
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        return LoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Cookie createCookie(String token) {

        Cookie cookie = new Cookie("token",token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        // Set the max age to 6 months (15,552,000 seconds)
        cookie.setMaxAge(15552000);
        return cookie;
    }

    @Override
    public LoginResponse oauthSuccess(String accessToken) {
       Long id= jwtService.getIdFromToken(accessToken);

       User user = userService.getUserById(id);
        String refreshToken= jwtService.createRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    private User saveUser(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setUsername(signupRequest.getUsername());
        return userService.saveUser(user);

    }

    private SignupResponse convertToSignup(User user) {

        return SignupResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
