package com.tnovel.demo.repository.user.entity.vo;

import java.util.Arrays;

public enum SignupType {
    NATIVE,
    KAKAO;

    public static SignupType findByName(String name) {
        return Arrays.stream(SignupType.values())
                .filter(each -> each.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 SignupType: " + name.toUpperCase()));
    }
}
