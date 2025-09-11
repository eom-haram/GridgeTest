package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.repository.post.entity.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponseDto {
    private Integer id;
    private String imageUrl;

    public static ImageResponseDto from(Image entity) {
        return new ImageResponseDto(
                entity.getId(),
                entity.getImageUrl()
        );
    }
}
