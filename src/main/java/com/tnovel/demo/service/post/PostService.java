package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private static final Integer PAGESIZE = 10;

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
    public Page<PostResponseDto> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, PAGESIZE, Sort.by("createdAt").descending());
        return postRepository.findByDataStatus(DataStatus.ACTIVATED, pageable)
                .map(PostResponseDto::from);
    }

    @Transactional
    public void delete(Integer id) {
        Post post =  this.internalFindById(id);
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
        return postRepository.findById(id)
                .filter(Post::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
    }

    @Transactional
    public boolean isPostOwner(Integer postId) {
        return this.internalFindById(postId).getUser().equals(userService.getLoggedUser());
    }
}
