package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.repository.post.entity.vo.PostReportCategory;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private PostReportCategory reportCategory;

    public static PostReport create(User user, Post post, PostReportCategory reportCategory) {
        return new PostReport(
                null,
                user,
                post,
                reportCategory
        );
    }
}