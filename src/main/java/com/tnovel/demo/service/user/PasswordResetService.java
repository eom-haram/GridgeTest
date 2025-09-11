package com.tnovel.demo.service.user;

import com.tnovel.demo.controller.user.dto.PasswordResetDto;
import com.tnovel.demo.controller.user.dto.PasswordResetRequestDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.user.PasswordResetTokenRepository;
import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.PasswordResetToken;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Transactional
    public void processResetRequest(PasswordResetRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        PasswordResetToken token = PasswordResetToken.generate(user);
        PasswordResetToken saved = tokenRepository.save(token);

        sendEmail(user.getEmail(), saved.getToken());
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

    private void sendEmail(String email, String token) {
        String resetUrl = "https://www.tnovel.com/reset-password?token=" + token;
        String body = "아래 링크를 클릭해 비밀번호를 재설정하세요.\n" + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정");
        message.setText(body);

        mailSender.send(message);
    }
}
