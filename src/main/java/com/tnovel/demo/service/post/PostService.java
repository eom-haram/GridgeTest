package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.PostResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.PostRepository;
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

    @Transactional
    public PostResponseDto findById(Integer id) {
        Post post = postRepository.findById(id)
                .filter(p -> p.getDataStatus().equals(DataStatus.ACTIVATED))
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
        return PostResponseDto.from(post);
    }

    @Transactional
    public List<PostResponseDto> findAll(String username) {
        return postRepository.findAll().stream()
                .filter(p -> userService.getFollowingUsers(username).contains(p.getUser()))
                .filter(p -> p.getDataStatus().equals(DataStatus.ACTIVATED))
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
    protected Post internalFindById(Integer id) {
        return postRepository.findById(id)
                .filter(p -> p.getDataStatus().equals(DataStatus.ACTIVATED))
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_EXIST));
    }
}
