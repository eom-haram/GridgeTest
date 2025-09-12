package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.CommentCreateRequestDto;
import com.tnovel.demo.controller.post.dto.CommentResponseDto;
import com.tnovel.demo.controller.post.dto.CommentUpdateRequestDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.post.CommentRepository;
import com.tnovel.demo.repository.post.entity.Comment;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto create(CommentCreateRequestDto request, String username) {
        Post post = postService.internalFindById(request.getPostId());
        Comment comment = post.addComment((User) userService.loadUserByUsername(username), request.getContent());
        return CommentResponseDto.from(comment);
    }

    @Transactional
    public CommentResponseDto update(CommentUpdateRequestDto request, String username) {
        Comment comment = this.internalFindById(request.getId());
        if (!comment.isCommentOwner((User) userService.loadUserByUsername(username))) {
            throw new CustomException(ExceptionType.COMMENT_NOT_YOURS);
        }
        comment.update(request.getContent());
        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.from(saved);
    }

    @Transactional
    protected Comment internalFindById(Integer id) {
        return commentRepository.findById(id)
                .filter(Comment::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.COMMENT_NOT_EXIST));
    }
}
