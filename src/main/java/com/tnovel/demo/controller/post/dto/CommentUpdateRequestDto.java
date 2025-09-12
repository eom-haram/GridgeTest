package com.tnovel.demo.controller.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    private Integer id;

    @Size(min = 1, message = "댓글은 최소 1자 입력해야 합니다.")
    private String content;
}
