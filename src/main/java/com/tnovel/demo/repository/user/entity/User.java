package com.tnovel.demo.repository.user.entity;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.post.entity.Post;
import com.tnovel.demo.repository.user.entity.vo.SignupType;
import com.tnovel.demo.repository.user.entity.vo.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Setter
    private String password;
    private String name;
    private String phNum;
    private String email;
    private LocalDate birthdate;

    private LocalDateTime createdAt;
    private SignupType signupType;
    private UserStatus userStatus;
    private DataStatus dataStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "followedUser")
    private List<Following> usersOneFollow;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "followingUser")
    private List<Following> usersWhoFollowOne;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Post> posts;

    @Transient
    private List<SimpleGrantedAuthority> authorities;
    public static SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    public static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    public static List<SimpleGrantedAuthority> SIMPLE_ROLES = List.of(ROLE_USER);
    public static List<SimpleGrantedAuthority> ADMIN_ROLES = List.of(ROLE_ADMIN);

    public static User create(String username, String password, String name, String phNum, String email, LocalDate birthdate) {
        return new User(
                null,
                username,
                password,
                name,
                phNum,
                email,
                birthdate,
                LocalDateTime.now(),
                SignupType.NATIVE,
                UserStatus.SIGNED_UP,
                DataStatus.ACTIVATED,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                SIMPLE_ROLES
        );
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public boolean isActivated() {
        return this.dataStatus.equals(DataStatus.ACTIVATED);
    }

    public void followUser(User targetUser) {
        Following newFollowing = Following.create(this, targetUser);

        if (this.usersOneFollow.isEmpty()) {
            this.usersOneFollow = new ArrayList<>();
        }
        this.usersOneFollow.add(newFollowing);


        if (targetUser.usersWhoFollowOne.isEmpty()) {
            targetUser.usersWhoFollowOne = new ArrayList<>();
        }
        targetUser.usersWhoFollowOne.add(newFollowing);
    }
}
