package com.tnovel.demo.repository.post;

import com.tnovel.demo.repository.post.entity.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findByIdWithLikes(Integer postId);

    Integer CountCommentsByPostId(Integer postId);
}
