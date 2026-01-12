package com.example.NatakaLK.util;

import com.example.NatakaLK.model.User;
import com.example.NatakaLK.model.enums.RoleType;
import com.example.NatakaLK.repo.UserRepo;
import com.example.NatakaLK.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${nataka.frontend.base.url}")
    private String frontendBaseUrl;

    private final JwtService jwtService;
    private final UserRepo userRepo;

    @Autowired
    public OAuth2LoginSuccessHandler(JwtService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepo.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);

            String[] nameParts = (name != null) ? name.split(" ") : new String[]{"User"};
            user.setFname(nameParts[0]);
            user.setLname(nameParts.length > 1 ? nameParts[1] : "");

            user.setStatus("approved");
            user.setRole(RoleType.Customer);
            userRepo.save(user);
        } else {
            boolean updated = false;
            String[] nameParts = (name != null) ? name.split(" ") : new String[]{user.getFname()};
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            if (!firstName.equals(user.getFname())) {
                user.setFname(firstName);
                updated = true;
            }
            if (!lastName.equals(user.getLname())) {
                user.setLname(lastName);
                updated = true;
            }
            if (updated) {
                userRepo.save(user);
            }
        }

        String jwtToken = jwtService.generateTokenByEmail(email);

        ResponseCookie cookie = ResponseCookie.from("JWT", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("None")
                .build();

        // Add Cookie to Response Header
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        getRedirectStrategy().sendRedirect(request, response, frontendBaseUrl + "/dashboard");
    }
}