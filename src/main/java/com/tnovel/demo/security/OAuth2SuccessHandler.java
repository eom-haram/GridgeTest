package com.tnovel.demo.security;

import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.security.vo.OAuth2UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final static Integer COOKIE_MAX_AGE = 60 * 60 * 24;

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2UserDetails principal = (OAuth2UserDetails) authenticationToken.getPrincipal();
        User user = (User) principal.getUser();

        String token = jwtProvider.generate(authenticationToken);
        String redirectUrl = JwtLoginFilter.getRedirectUrl(request, response);
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(COOKIE_MAX_AGE)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect(redirectUrl);
    }
}
