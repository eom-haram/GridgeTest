package com.tnovel.demo.controller.user.dto;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.repository.user.entity.vo.SignupType;
import com.tnovel.demo.repository.user.entity.vo.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String phNum;
    private LocalDate birthdate;
    private LocalDateTime createdAt;
    private SignupType singnupType;
    private UserStatus userStatus;
    private DataStatus dataStatus;

    public static UserResponseDto of(User entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getUsername(),
                entity.getName(),
                entity.getPhNum(),
                entity.getBirthdate(),
                entity.getCreatedAt(),
                entity.getSignupType(),
                entity.getUserStatus(),
                entity.getDataStatus()
        );
    }
}
