package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.PostReport;
import com.tnovel.demo.repository.post.entity.vo.PostReportCategory;
import com.tnovel.demo.repository.post.entity.vo.PostStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostReportResponseDto {
    private Integer id;
    private UserSimpleResponseDto user;
    private PostReportCategory reportCategory;
    private Integer postId;
    private PostStatus postStatus;

    public static PostReportResponseDto from(PostReport entity) {
        return new PostReportResponseDto(
                entity.getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getReportCategory(),
                entity.getPost().getId(),
                entity.getPost().getPostStatus()
                );
    }
}