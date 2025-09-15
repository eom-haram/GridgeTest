package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.repository.post.entity.vo.PostReportCategory;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PostReportCreateRequestDto {
    @Min(1)
    private Integer postId;

    private PostReportCategory reportCategory;
}