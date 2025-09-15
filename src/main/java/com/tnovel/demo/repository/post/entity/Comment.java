package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private DataStatus dataStatus;

    protected static Comment create(User user, Post post, String content) {
        return new Comment(
                null,
                user,
                post,
                content,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()),
                DataStatus.ACTIVATED
        );
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Timestamp.from(Instant.now());
    }

    protected void delete() {
        this.dataStatus = DataStatus.DELETED;
    }

    public boolean isActivated() {
        return this.dataStatus.equals(DataStatus.ACTIVATED);
    }
}