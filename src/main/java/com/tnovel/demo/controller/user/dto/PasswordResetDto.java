package com.tnovel.demo.controller.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordResetDto {
    private String token;

    @Size(min = 6, max = 20)
    private String password;
}
