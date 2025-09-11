package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.Like;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeResponseDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private LocalDateTime createdAt;

    public static LikeResponseDto from(Like entity) {
        return new LikeResponseDto(
                entity.getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getCreatedAt()
        );
    }
}
