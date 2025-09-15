package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostScrollViewDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private List<String> images;
    private String contentPreview;
    private Integer likeCount;
    private Integer commentCount;
    private List<CommentResponseDto> resentComments;
}