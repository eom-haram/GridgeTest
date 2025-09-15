package com.tnovel.demo.service.user;

import com.tnovel.demo.controller.user.dto.PasswordResetDto;
import com.tnovel.demo.controller.user.dto.PasswordResetRequestDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.user.PasswordResetTokenRepository;
import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.PasswordResetToken;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void processResetRequest(PasswordResetRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        PasswordResetToken token = PasswordResetToken.generate(user);
        PasswordResetToken saved = tokenRepository.save(token);

        String resetUrl = "https://www.tnovel.com/reset-password?token=" + saved.getToken();
        String content = "아래 링크를 클릭해 비밀번호를 재설정하세요.\n" + resetUrl;
        String subject = "비밀번호 재설정";

        emailService.sendEmail(user.getEmail(), subject, content);
    }

    @Transactional
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken dbToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(ExceptionType.TOKEN_NOT_EXIT));
        if (dbToken.isExpired()) {
            throw new CustomException(ExceptionType.TOKEN_EXPIRED);
        }

        return dbToken;
    }

    @Transactional
    public void resetPassword(PasswordResetDto request) {
        PasswordResetToken token = this.validateToken(request.getToken());
        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        user.resetPassword(passwordEncoder.encode(request.getPassword()));

        tokenRepository.delete(token);
    }
}
