package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.LikeCreateRequestDto;
import com.tnovel.demo.controller.post.dto.LikeResponseDto;
import com.tnovel.demo.repository.post.entity.Like;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public LikeResponseDto create(LikeCreateRequestDto request, String username) {
        Post post = postService.internalFindById(request.getPostId());
        Like like = post.addLike((User) userService.loadUserByUsername(username));
        return LikeResponseDto.from(like);
    }
}
