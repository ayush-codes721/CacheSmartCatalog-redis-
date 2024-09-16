package CacheSmartCatalog.service.Auth;


import CacheSmartCatalog.request.LoginRequest;
import CacheSmartCatalog.request.SignupRequest;
import CacheSmartCatalog.response.LoginResponse;
import CacheSmartCatalog.response.SignupResponse;
import jakarta.servlet.http.Cookie;

public interface IAuthService {

    SignupResponse signup(SignupRequest signupRequest);
    LoginResponse login(LoginRequest loginRequest);
    Cookie createCookie(String token);
    LoginResponse oauthSuccess(String accessToken);
}
