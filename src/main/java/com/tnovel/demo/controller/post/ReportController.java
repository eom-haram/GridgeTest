package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.ReportCreateRequestDto;
import com.tnovel.demo.controller.post.dto.ReportResponseDto;
import com.tnovel.demo.service.post.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("")
    public ResponseEntity<ReportResponseDto> create(@Valid ReportCreateRequestDto request) {
        ReportResponseDto report = reportService.create(request);
        return ResponseEntity.ok(report);
    }
}
