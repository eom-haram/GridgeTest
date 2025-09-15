package com.tnovel.demo.controller.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostCreateRequestDto {
    @Size(min = 1, max = 10, message = "사진은 최소 1장, 최대 10장 등록 가능합니다.")
    private List<MultipartFile> images;

    @Size(max = 150)
    private String content;
}
