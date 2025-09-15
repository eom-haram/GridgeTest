package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static CommentResponseDto from(Comment entity) {
        return new CommentResponseDto(
                entity.getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}