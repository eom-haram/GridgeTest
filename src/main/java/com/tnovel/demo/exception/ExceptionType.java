package com.tnovel.demo.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "사용할 수 없는 사용자 이름입니다. 다른 이름을 사용하세요."),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    TOKEN_NOT_EXIT(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "해당 토큰은 만료되었습니다."),
    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    LIKE_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 좋아요가 존재하지 않습니다."),
    COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    POST_NOT_YOURS(HttpStatus.BAD_REQUEST, "해당 게시글에 대한 권한이 없습니다."),
    COMMENT_NOT_YOURS(HttpStatus.BAD_REQUEST, "해당 댓글에 대한 권한이 없습니다."),
    LIKE_NOT_YOURS(HttpStatus.BAD_REQUEST, "해당 좋아요에 대한 권한이 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.");

    private final HttpStatus status;
    private final String message;
}
