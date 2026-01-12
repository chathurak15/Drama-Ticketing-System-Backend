package com.example.NatakaLK.util;

import com.example.NatakaLK.config.CustomOAuth2User;
import com.example.NatakaLK.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
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

    public OAuth2LoginSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        String email = oauthUser.getEmail();
        String jwtToken = jwtService.generateTokenByEmail(email);

        String targetUrl = frontendBaseUrl + "/dashboard?token=" + jwtToken;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
