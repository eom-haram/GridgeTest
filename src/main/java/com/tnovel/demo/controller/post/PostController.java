package com.tnovel.demo.controller.post;

import com.tnovel.demo.controller.post.dto.CommentResponseDto;
import com.tnovel.demo.controller.post.dto.PostCreateRequestDto;
import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.service.post.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private static final String POST_OWNER_OR_ADMIN = "@postService.isPostOwner(#postId) or hasRole('ROLE_ADMIN')";

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> singlePost(@PathVariable @Min(1) Integer id) {
        PostResponseDto post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/comments/")
    public ResponseEntity<Page<CommentResponseDto>> commentsForSinglePost(@PathVariable @Min(1) Integer postId, @RequestParam Integer pageIndex, @RequestParam Integer size) {
        Page<CommentResponseDto> comments = postService.findComments(postId, pageIndex, size);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("")
    public ResponseEntity<PostResponseDto> create(@Valid PostCreateRequestDto request) throws IOException {
        PostResponseDto post = postService.create(request);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Integer postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN + "or @commentService.isCommentOwner(#commentId)")
    public ResponseEntity<Void> deleteComment(@PathVariable @Min(1) Integer postId, @PathVariable @Min(1) Integer commentId) {
        postService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{postId}/likes/{likeId}")
    @PreAuthorize(POST_OWNER_OR_ADMIN + "or @likeService.isLikeOwner(#likeId)")
    public ResponseEntity<Void> deleteLike(@PathVariable @Min(1) Integer postId, @PathVariable @Min(1) Integer likeId) {
        postService.deleteLike(postId, likeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
