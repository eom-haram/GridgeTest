package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.CommentCreateRequestDto;
import com.tnovel.demo.controller.post.dto.CommentResponseDto;
import com.tnovel.demo.controller.post.dto.CommentUpdateRequestDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.CommentRepository;
import com.tnovel.demo.repository.post.entity.Comment;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto create(CommentCreateRequestDto request) {
        Post post = postService.internalFindById(request.getPostId());
        Comment comment = post.addComment(userService.getLoggedUser(), request.getContent());
        return CommentResponseDto.from(comment);
    }

    @Transactional
    public CommentResponseDto update(CommentUpdateRequestDto request) {
        Comment comment = this.internalFindById(request.getId());
        comment.update(request.getContent());
        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.from(saved);
    }

    @Transactional
    protected Comment internalFindById(Integer id) {
        return commentRepository.findByIdAndDataStatus(id, DataStatus.ACTIVATED)
                .orElseThrow(() -> new CustomException(ExceptionType.COMMENT_NOT_EXIST));
    }

    @Transactional
    public boolean isCommentOwner(Integer commentId) {
        return this.internalFindById(commentId).getUser().equals(userService.getLoggedUser());
    }
}