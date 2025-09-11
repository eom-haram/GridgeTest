package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.service.post.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable @Min(value = 1) Integer postId) {
        PostResponseDto post = postService.findById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails) {
        List<PostResponseDto> posts = postService.findAll(userDetails.getUsername());
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        postService.delete(postId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
