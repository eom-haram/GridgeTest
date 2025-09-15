package com.tnovel.demo.repository.user.entity;

import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.entity.vo.SignupType;
import com.tnovel.demo.repository.user.entity.vo.UserStatus;
import com.tnovel.demo.security.vo.OAuth2Resource;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
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

    private Timestamp createdAt;
    private SignupType signupType;
    private Timestamp privacyConsentDate;
    private UserStatus userStatus;
    private DataStatus dataStatus;
    private String role;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
//    private List<Post> posts;

//    @Transient
//    private List<SimpleGrantedAuthority> authorities;
    public static SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    public static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
//    public static List<SimpleGrantedAuthority> SIMPLE_ROLES = List.of(ROLE_USER);
//    public static List<SimpleGrantedAuthority> ADMIN_ROLES = List.of(ROLE_ADMIN);


    public static User create(String username, String password, String name, String phNum, String email, LocalDate birthdate) {
        return new User(
                null,
                username,
                password,
                name,
                phNum,
                email,
                birthdate,
                Timestamp.from(Instant.now()),
                SignupType.NATIVE,
                Timestamp.from(Instant.now()),
                UserStatus.SIGNED_UP,
                DataStatus.ACTIVATED,
//                Collections.emptyList(),
                "ROLE_USER"
        );
    }

    public static User create(OAuth2Resource resource) {
        return new User(
                null,
                resource.getProviderId().toString(),
                null,
                null,
                null,
                resource.getEmail(),
                null,
                Timestamp.from(Instant.now()),
                resource.getProvider(),
                Timestamp.from(Instant.now()),
                UserStatus.SIGNED_UP,
                DataStatus.ACTIVATED,
                "ROLE_USER"
//                SIMPLE_ROLES
        );
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public boolean isActivated() {
        return this.dataStatus.equals(DataStatus.ACTIVATED);
    }

    public boolean donePrivacyConsent() {
        return this.privacyConsentDate.after(Timestamp.valueOf(LocalDateTime.now().minusYears(1)));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }
}
