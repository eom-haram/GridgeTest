package com.tnovel.demo.controller.user.dto;

import com.tnovel.demo.repository.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSimpleResponseDto {
    private Integer id;
    private String username;

    public static UserSimpleResponseDto from(User entity) {
        return new UserSimpleResponseDto(
                entity.getId(),
                entity.getUsername()
        );
    }
}
