package com.tnovel.demo.security;

import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.repository.user.entity.vo.SignupType;
import com.tnovel.demo.security.vo.OAuth2KakaoResource;
import com.tnovel.demo.security.vo.OAuth2Resource;
import com.tnovel.demo.security.vo.OAuth2UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest resourceRequest) throws OAuth2AuthenticationException {
        final OAuth2User resourceResponse = super.loadUser(resourceRequest);
        final OAuth2Resource resource = this.extract(
                resourceRequest.getClientRegistration().getRegistrationId(),
                resourceResponse
        );

        UserDetails retrieved = userRepository.findByUsername(resource.getProviderId().toString())
                .orElseGet(() -> userRepository.save(User.create(resource)));

        return OAuth2UserDetails.create(resource, retrieved);
    }

    private OAuth2Resource extract(String provider, OAuth2User resourceResponse) {
        return switch (SignupType.findByName(provider)) {
            case KAKAO -> OAuth2KakaoResource.create(resourceResponse);
            default -> throw new IllegalArgumentException("존재하지 않는 RegistrationId: " + provider);
        };
    }
}
