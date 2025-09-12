package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Image> images;

    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DataStatus dataStatus;

    public static Post create(User user, String content) {
        return new Post(
                null,
                user,
                new ArrayList<>(),
                content,
                Collections.emptyList(),
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                DataStatus.ACTIVATED
        );
    }

    public void delete() {
        this.dataStatus = DataStatus.DELETED;
    }

    public boolean isPostOwner(User user) {
        return this.user.equals(user);
    }

    public boolean isActivated() {
        return this.dataStatus.equals(DataStatus.ACTIVATED);
    }

    public Post addImages(List<String> imageUrls) {
        for (String url:imageUrls) {
            this.images.add(Image.create(this, url));
        }

        return this;
    }

    public Like addLike(User user) {
        if (this.likes.isEmpty()) {
            this.likes = new ArrayList<>();
        }
        Like like = Like.create(user, this);
        this.likes.add(like);

        return like;
    }

    public void deleteLike(Like like) {
        like.delete();
        this.likes.remove(like);
    }

    public Comment addComment(User user, String content) {
        if (this.comments.isEmpty()) {
            this.comments = new ArrayList<>();
        }
        Comment comment = Comment.create(user, this, content);
        this.comments.add(comment);
        return comment;
    }

    public void deleteComment(Comment comment) {
        comment.delete();
        this.comments.remove(comment);
    }
}
