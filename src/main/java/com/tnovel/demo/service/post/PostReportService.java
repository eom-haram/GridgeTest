package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.PostReportCreateRequestDto;
import com.tnovel.demo.controller.post.dto.PostReportResponseDto;
import com.tnovel.demo.repository.post.PostReportRepository;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.repository.post.entity.PostReport;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostReportService {
    private final PostReportRepository reportRepository;
    private final UserService userService;
    private final PostService postService;

    private static Integer EXAM_CRI = 10;
    private static String WARN_MSG = "해당 게시글은 검토가 필요합니다. postId: ";

    @Transactional
    public PostReportResponseDto create(PostReportCreateRequestDto request) {
        Post post = postService.internalFindById(request.getPostId());
        PostReport report = PostReport.create(userService.getLoggedUser(), post, request.getReportCategory());
        PostReport saved = reportRepository.save(report);

        post.addReports();
        if (post.getReportNum().equals(EXAM_CRI)) {
            log.warn(WARN_MSG + post.getId());
            post.goingUnderExamination();
        }
        return PostReportResponseDto.from(saved);
    }
}