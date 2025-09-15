package com.tnovel.demo.security.vo;

import com.tnovel.demo.repository.user.entity.vo.SignupType;

import java.util.Map;

public interface OAuth2Resource {
    SignupType getProvider();
    Long getProviderId();
    Map<String, Object> getAttributes();
    Map<String, Object> getAccount();
    Map<String, Object> getProfile();
    String getEmail();
}
