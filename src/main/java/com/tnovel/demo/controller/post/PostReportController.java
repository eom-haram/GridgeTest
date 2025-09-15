package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.PostReportCreateRequestDto;
import com.tnovel.demo.controller.post.dto.PostReportResponseDto;
import com.tnovel.demo.service.post.PostReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class PostReportController {
    private final PostReportService reportService;

    @PostMapping("")
    public ResponseEntity<PostReportResponseDto> create(@Valid PostReportCreateRequestDto request) {
        PostReportResponseDto report = reportService.create(request);
        return ResponseEntity.ok(report);
    }
}