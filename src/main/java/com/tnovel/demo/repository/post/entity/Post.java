package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime modifiedAt;

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

    public Post addImages(List<String> imageUrls) {
        for (String url:imageUrls) {
            this.images.add(Image.create(this, url));
        }

        return this;
    }

    public void addLike(User user) {
        if (this.likes.isEmpty()) {
            this.likes = new ArrayList<>();
        }
        this.likes.add(Like.create(user, this));
    }

    public void deleteLike(Integer likeId) {
        Like like = this.likes.stream()
                .filter(l -> l.getId().equals(likeId))
                .filter(l -> l.getDataStatus().equals(DataStatus.ACTIVATED))
                .findFirst()
                .orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_EXIST));

        like.delete();
        this.likes.remove(like);
    }

    public void addComment(User user, String content) {
        if (this.comments.isEmpty()) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(Comment.create(user, this, content));
    }

    public void modifyComment(Integer commentId, String content) {
        Comment comment = this.findCommentById(commentId);
        comment.modify(content);
    }

    public void deleteComment(Integer commentId) {
        Comment comment = this.findCommentById(commentId);
        comment.delete();
    }

    private Comment findCommentById(Integer commentId) {
        return this.comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .filter(c -> c.getDataStatus().equals(DataStatus.ACTIVATED))
                .findFirst()
                .orElseThrow(() -> new CustomException(ExceptionType.COMMENT_NOT_EXIST));
    }
}
