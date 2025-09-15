package com.tnovel.demo.repository.post.entity;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.entity.vo.PostStatus;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private PostStatus postStatus;
    private Integer reportNum;
    private DataStatus dataStatus;

    public static Post create(User user, String content) {
        return new Post(
                null,
                user,
                new ArrayList<>(),
                content,
                Collections.emptyList(),
                Collections.emptyList(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()),
                PostStatus.VISIBLE,
                0,
                DataStatus.ACTIVATED
        );
    }

    public void delete() {
        this.dataStatus = DataStatus.DELETED;
        for (Comment comment : this.comments) {
            deleteComment(comment);
        }
        for (Like like : this.likes) {
            deleteLike(like);
        }
    }

    public void addImages(List<String> imageUrls) {
        for (String url:imageUrls) {
            this.images.add(Image.create(this, url));
        }
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

    public void addReports() {
        this.reportNum++;
    }

    public void goingUnderExamination() {
        this.postStatus = PostStatus.INVISIBLE;
    }
}
