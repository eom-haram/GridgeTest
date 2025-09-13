package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.service.post.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private static final String POST_OWNER_OR_ADMIN = "@postService.isPostOwner(#postId) or hasRole('ROLE_ADMIN')";

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable @Min(1) Integer postId) {
        PostResponseDto post = postService.findById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails) {
        List<PostResponseDto> posts = postService.findAll(userDetails.getUsername());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("")
    public ResponseEntity<Page<PostResponseDto>> findAll(@RequestParam Integer page) {
        Page<PostResponseDto> posts = postService.findAll(page);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Integer postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN + "or @commentService.isCommentOwner(#commentId)")
    public ResponseEntity<Void> deleteComment(@PathVariable @Min(1) Integer postId, @PathVariable @Min(1) Integer commentId) {
        postService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{postId}/likes/{likeId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN + "or @likeService.isLikeOwner(#likeId)")
    public ResponseEntity<Void> deleteLike(@PathVariable @Min(1) Integer postId, @PathVariable @Min(1) Integer likeId) {
        postService.deleteLike(postId, likeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
