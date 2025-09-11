package com.tnovel.demo.repository.post;

import com.tnovel.demo.repository.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
