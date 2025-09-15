package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.Image;
import com.tnovel.demo.repository.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private List<String> images;
    private String content;
    private Integer likeNum;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PostResponseDto from(Post entity) {
        return new PostResponseDto(
                entity.getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getImages().stream().map(Image::getImageUrl).toList(),
                entity.getContent(),
                entity.getLikes().size(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
