package com.tnovel.demo.repository.post;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.entity.Comment;
import com.tnovel.demo.repository.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByIdAndDataStatus(Integer id, DataStatus dataStatus);

    Page<Comment> findByDataStatusAndPost(DataStatus dataStatus, Post post, Pageable pageable);
}