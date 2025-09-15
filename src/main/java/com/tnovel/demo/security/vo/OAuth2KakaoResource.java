package com.tnovel.demo.security.vo;

import com.tnovel.demo.repository.user.entity.vo.SignupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2KakaoResource implements OAuth2Resource{
    public SignupType provider;
    public Long providerId;
    public Map<String, Object> attributes;
    public Map<String, Object> account;
    public Map<String, Object> profile;
    public String email;

    public static OAuth2KakaoResource create(OAuth2User resourceResponse) {
        final Map<String, Object> attributes = resourceResponse.getAttributes();
        final Map<String, Object> account = (Map<String,Object>) attributes.get("kakao_account");
        return new OAuth2KakaoResource(
                SignupType.KAKAO,
                (Long) attributes.get("id"),
                attributes,
                account,
                (Map<String, Object>) account.get("profile"),
                (String) account.get("email")
        );
    }
}
