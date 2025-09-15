package com.tnovel.demo.repository.post;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.entity.Post;
import static com.tnovel.demo.repository.post.entity.QComment.comment;
import static com.tnovel.demo.repository.post.entity.QLike.like;
import static com.tnovel.demo.repository.post.entity.QPost.post;

import com.tnovel.demo.repository.post.entity.vo.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Post> findByIdWithLikes(Integer postId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.likes, like).fetchJoin()
                .where(post.id.eq(postId)
                        .and(post.dataStatus.eq(DataStatus.ACTIVATED))
                        .and(post.postStatus.eq(PostStatus.VISIBLE)))
                .fetchOne());
    }

    @Override
    public Integer CountCommentsByPostId(Integer postId) {
        return jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId)
                        .and(comment.dataStatus.eq(DataStatus.ACTIVATED)))
                .fetchOne().intValue();
    }
}
