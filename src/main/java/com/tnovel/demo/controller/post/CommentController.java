package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.CommentCreateRequestDto;
import com.tnovel.demo.controller.post.dto.CommentResponseDto;
import com.tnovel.demo.controller.post.dto.CommentUpdateRequestDto;
import com.tnovel.demo.service.post.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentResponseDto> create(@Valid CommentCreateRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto comment = commentService.create(request, userDetails.getUsername());
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@Valid CommentUpdateRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto comment = commentService.update(request, userDetails.getUsername());
        return ResponseEntity.ok(comment);
    }
}
