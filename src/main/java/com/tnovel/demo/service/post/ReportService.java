package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.ReportCreateRequestDto;
import com.tnovel.demo.controller.post.dto.ReportResponseDto;
import com.tnovel.demo.repository.post.ReportRepository;
import com.tnovel.demo.repository.post.entity.Report;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public ReportResponseDto create(ReportCreateRequestDto request) {
        Report report = Report.create(userService.getLoggedUser(), postService.internalFindById(request.getPostId()), request.getReportCategory());
        Report saved = reportRepository.save(report);

        return ReportResponseDto.from(saved);
    }
}
