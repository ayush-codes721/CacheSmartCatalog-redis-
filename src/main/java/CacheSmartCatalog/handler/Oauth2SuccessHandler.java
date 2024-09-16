package CacheSmartCatalog.handler;

import CacheSmartCatalog.model.User;
import CacheSmartCatalog.service.JWT.JwtService;
import CacheSmartCatalog.service.User.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final IUserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userService.getUserByUsername(email);

        User user1 = null;
        if (!user.isPresent()) {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setName(oAuth2User.getName());
            user1 = userService.saveUser(newUser);

        } else {
            user1 = user.get();

        }
        String accessToken = jwtService.createRefreshToken(user1);

        String redirectUrl = String.format("http://localhost:5000/auth/oauth-success?token=%s", accessToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);


    }
}
