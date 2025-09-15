package com.tnovel.demo.repository.post;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
    Optional<Post> findByIdAndDataStatus(Integer id, DataStatus dataStatus);
}
