package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime createdAt;

    private DataStatus dataStatus;

    protected static Like create(User user, Post post) {
        return new Like(
                null,
                user,
                post,
                LocalDateTime.now(),
                DataStatus.ACTIVATED
        );
    }

    protected void delete() {
        this.dataStatus = DataStatus.DELETED;
    }

    public boolean isActivated() {
        return this.dataStatus.equals(DataStatus.ACTIVATED);
    }
}
