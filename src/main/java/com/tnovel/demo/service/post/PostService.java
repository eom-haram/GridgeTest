package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.CommentResponseDto;
import com.tnovel.demo.controller.post.dto.PostCreateRequestDto;
import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.CommentRepository;
import com.tnovel.demo.repository.post.PostRepository;
import com.tnovel.demo.repository.post.entity.Comment;
import com.tnovel.demo.repository.post.entity.Like;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final ImageService imageService;

    private static Integer PAGESIZE = 10;

    @Transactional
    public PostResponseDto findById(Integer id) {
        Post post =  postRepository.findByIdWithLikes(id)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
        return PostResponseDto.from(post);
    }

    @Transactional
    public Page<CommentResponseDto> findComments(Integer postId, Integer pageIndex, Integer size) {
        Post post = this.internalFindById(postId);
        if (!size.equals(PAGESIZE) || pageIndex != (postRepository.CountCommentsByPostId(postId) % 10 + 1)) {
            throw new CustomException(ExceptionType.WRONG_PAGE_INDEX);
        }
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("createdAt").descending());
        return commentRepository.findByDataStatusAndPost(DataStatus.ACTIVATED, post, pageable)
                .map(CommentResponseDto::from);
    }

    @Transactional
    public PostResponseDto create(PostCreateRequestDto request) throws IOException {
        Post post = Post.create(userService.getLoggedUser(), request.getContent());
        post.addImages(imageService.uploadImages(request.getImages()));
        Post saved = postRepository.save(post);
        return PostResponseDto.from(saved);
    }

    @Transactional
    public void delete(Integer id) {
        Post post = this.internalFindById(id);
        post.delete();
        postRepository.save(post);
    }

    @Transactional
    public void deleteComment(Integer postId, Integer commentId) {
        Post post = this.internalFindById(postId);
        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .filter(Comment::isActivated).findFirst()
                .orElseThrow(() -> new CustomException(ExceptionType.COMMENT_NOT_EXIST));
        post.deleteComment(comment);
        postRepository.save(post);
    }

    @Transactional
    public void deleteLike(Integer postId, Integer likeId) {
        Post post = this.internalFindById(postId);
        Like like = post.getLikes().stream()
                .filter(l -> l.getId().equals(likeId))
                .filter(Like::isActivated).findFirst()
                .orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_EXIST));
        post.deleteLike(like);
        postRepository.save(post);
    }

    @Transactional
    protected Post internalFindById(Integer id) {
        return postRepository.findByIdAndDataStatus(id, DataStatus.ACTIVATED)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
    }
}
