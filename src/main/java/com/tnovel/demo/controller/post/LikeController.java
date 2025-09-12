package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.LikeCreateRequestDto;
import com.tnovel.demo.controller.post.dto.LikeResponseDto;
import com.tnovel.demo.service.post.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @PostMapping
    public ResponseEntity<LikeResponseDto> create(@Valid LikeCreateRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        LikeResponseDto like = likeService.create(request, userDetails.getUsername());
        return ResponseEntity.ok(like);
    }
}
