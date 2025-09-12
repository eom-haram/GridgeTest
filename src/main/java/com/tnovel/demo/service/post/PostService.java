package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.post.PostRepository;
import com.tnovel.demo.repository.post.entity.Comment;
import com.tnovel.demo.repository.post.entity.Like;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Transactional
    public PostResponseDto findById(Integer id) {
        Post post = postRepository.findById(id)
                .filter(Post::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
        return PostResponseDto.from(post);
    }

    @Transactional
    public List<PostResponseDto> findAll(String username) {
        return postRepository.findAll().stream()
                .filter(p -> userService.getFollowingUsers(username).contains(p.getUser()))
                .filter(Post::isActivated)
                .map(PostResponseDto::from)
                .toList();
    }

    @Transactional
    public void delete(Integer id, String username) {
        Post post =  this.internalFindById(id);
        if (!post.isPostOwner((User) userService.loadUserByUsername(username))) {
            throw new CustomException(ExceptionType.POST_NOT_YOURS);
        }
        post.delete();
        postRepository.save(post);
    }

    @Transactional
    public void deleteComment(Integer postId, Integer commentId, String username) {
        Post post = this.internalFindById(postId);
        Comment comment = commentService.internalFindById(commentId);
        if (!post.getComments().contains(comment)) {
            throw new CustomException(ExceptionType.BAD_REQUEST);
        }
        if (!comment.isCommentOwner((User) userService.loadUserByUsername(username))) {
            throw new CustomException(ExceptionType.COMMENT_NOT_YOURS);
        }

        post.deleteComment(comment);
        postRepository.save(post);
    }

    @Transactional
    public void deleteLike(Integer postId, Integer likeId, String username) {
        Post post = this.internalFindById(postId);
        Like like = post.getLikes().stream()
                .filter(l -> l.getId().equals(likeId))
                .filter(Like::isActivated).findFirst()
                .orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_EXIST));
        if (!like.isLikeOwner((User) userService.loadUserByUsername(username))) {
            throw new CustomException(ExceptionType.LIKE_NOT_YOURS);
        }

        post.deleteLike(like);
        postRepository.save(post);
    }

    @Transactional
    protected Post internalFindById(Integer id) {
        return postRepository.findById(id)
                .filter(Post::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
    }
}
