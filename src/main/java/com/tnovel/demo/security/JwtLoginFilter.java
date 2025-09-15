package com.tnovel.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final static String REDIRECT_DEFAULT_PAGE = "/list";
    private final static Integer COOKIE_MAX_AGE = 60 * 60 * 24;

    private JwtProvider jwtProvider;
    private ObjectMapper objectMapper;

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        String token = jwtProvider.generate(authentication);
        String redirectUrl = getRedirectUrl(request, response);
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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorMessage;
        if (failed instanceof UsernameNotFoundException) {
            errorMessage = "입력한 사용자 이름을 사용하는 계정을 찾을 수 없습니다. 사용자 이름을 확인하고 다시 시도하세요.";
        } else if (failed instanceof BadCredentialsException) {
            errorMessage = "잘못된 비밀번호입니다. 다시 확인하세요.";
        } else {
            errorMessage = "인증에 실패했습니다. 다시 확인하세요.";
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication Failed");
        errorResponse.put("message", errorMessage);

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }

    public static String getRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        return savedRequest != null ? savedRequest.getRedirectUrl() : REDIRECT_DEFAULT_PAGE;
    }
}
