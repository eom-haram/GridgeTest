package com.tnovel.demo.service.post;

import com.tnovel.demo.controller.post.dto.LikeCreateRequestDto;
import com.tnovel.demo.controller.post.dto.LikeResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.post.LikeRepository;
import com.tnovel.demo.repository.post.entity.Like;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public LikeResponseDto create(LikeCreateRequestDto request) {
        Post post = postService.internalFindById(request.getPostId());
        Like like = post.addLike(userService.getLoggedUser());
        return LikeResponseDto.from(like);
    }

    @Transactional
    protected Like internalFindById(Integer id) {
        return likeRepository.findById(id)
                .filter(Like::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_EXIST));
    }
    @Transactional
    public boolean isLikeOwner(Integer likeId) {
        return this.internalFindById(likeId).getUser().equals(userService.getLoggedUser());
    }
}
