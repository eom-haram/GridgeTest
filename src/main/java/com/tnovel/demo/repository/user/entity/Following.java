package com.tnovel.demo.repository.user.entity;

import com.tnovel.demo.repository.DataStatus;
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
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_usr_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_usr_id")
    private User followedUser;

    private LocalDateTime createdAt;
    private DataStatus dataStatus;

    protected static Following create(User followingUser, User followedUser) {
        return new Following(
                null,
                followingUser,
                followedUser,
                LocalDateTime.now(),
                DataStatus.ACTIVATED
        );
    }
}
