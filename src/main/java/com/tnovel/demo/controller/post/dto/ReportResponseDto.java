package com.tnovel.demo.controller.post.dto;

import com.tnovel.demo.controller.user.dto.UserSimpleResponseDto;
import com.tnovel.demo.repository.post.entity.Report;
import com.tnovel.demo.repository.post.entity.vo.ReportCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportResponseDto {
    private Integer id;
    private Integer postId;
    private UserSimpleResponseDto user;
    private ReportCategory reportCategory;

    public static ReportResponseDto from(Report entity) {
        return new ReportResponseDto(
                entity.getId(),
                entity.getPost().getId(),
                UserSimpleResponseDto.from(entity.getUser()),
                entity.getReportCategory()
        );
    }
}
