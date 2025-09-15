package com.tnovel.demo.repository.post.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostReportCategory {
    SPAM("스팸"),
    PROVOCATIVE("나체 이미지 또는 성적 행위"),
    HATRED("혐오 발언 또는 상징"),
    VIOLENCE("폭력 또는 위험한 단체"),
    ILLEGAL_PRODUCT("불법 또는 규제 상품 판매"),
    BULLY("따돌림 또는 괴롭힘"),
    IP_INFRINGEMENT("지식재산권 침해"),
    SUICIDAL("자살 또는 자해"),
    ED("섭식 장애"),
    FRAUD("사기 또는 거짓"),
    FALSE_INFO("거짓 정보"),
    PERSONAL("마음에 들지 않습니다.");

    private final String description;
}