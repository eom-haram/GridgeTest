package com.tnovel.demo.controller.user;

import com.tnovel.demo.controller.user.dto.PasswordResetDto;
import com.tnovel.demo.controller.user.dto.PasswordResetRequestDto;
import com.tnovel.demo.service.user.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService resetService;

    @PostMapping("/request")
    public ResponseEntity<String> passwordResetRequest(@RequestBody @Valid PasswordResetRequestDto request) {
        resetService.processResetRequest(request);
        return ResponseEntity.ok("가입 시 등록한 이메일로 비밀번호 재설정 링크가 전송되었습니다.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        resetService.validateToken(token);
        return ResponseEntity.ok("토큰의 유효성이 확인되었습니다.");
    }

    @PutMapping("/password/reset")
    public ResponseEntity<String> passwordReset(@RequestBody @Valid PasswordResetDto request) {
        resetService.resetPassword(request);
        return ResponseEntity.ok("비밀번호가 재설정되었습니다.");
    }
}
