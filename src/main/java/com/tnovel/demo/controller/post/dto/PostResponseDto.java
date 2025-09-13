package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponseDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private List<ImageResponseDto> images;
    private String content;
    private List<LikeResponseDto> likes;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponseDto from(Post entity) {
        return new PostResponseDto(
                entity.getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getImages().stream().map(ImageResponseDto::from).toList(),
                entity.getContent(),
                entity.getLikes().stream().map(LikeResponseDto::from).toList(),
                entity.getComments().stream().map(CommentResponseDto::from).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
