package com.tnovel.demo.repository.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String token;
    private Timestamp expiresAt;

    public static PasswordResetToken generate(User user) {
        return new PasswordResetToken(
                null,
                user,
                UUID.randomUUID().toString(),
                Timestamp.valueOf(LocalDateTime.now().plusHours(1))
        );
    }

    public boolean isExpired() {
        return expiresAt.before(Timestamp.from(Instant.now()));
    }
}
