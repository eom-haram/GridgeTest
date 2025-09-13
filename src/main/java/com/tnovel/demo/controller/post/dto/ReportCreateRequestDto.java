package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.repository.post.entity.vo.ReportCategory;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ReportCreateRequestDto {
    @Min(1)
    private Integer postId;

    private ReportCategory reportCategory;
}
