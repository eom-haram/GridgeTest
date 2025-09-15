package com.tnovel.demo.controller.post.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class LikeCreateRequestDto {
    @Min(1)
    private Integer postId;
}