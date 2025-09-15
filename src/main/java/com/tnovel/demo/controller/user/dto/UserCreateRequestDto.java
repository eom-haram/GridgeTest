package com.tnovel.demo.controller.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-z0-9_\\.]+$", message = "사용자 이름에는 문자, 숫자, 밑줄 및 마침표만 사용할 수 있습니다.")
    private String username;

    @Size(min = 6, max = 20)
    private String password;

    @Size(max = 20)
    private String name;

    @Size(max = 20)
    @Pattern(regexp = "^+(\\d{1,4})[-]?(\\d{3,4})[-]?(\\d{3,4})$", message = "휴대폰 번호가 정확하지 않습니다. 국가번호를 포함하여 전체 전화번호를 입력해주세요.")
    private String phNum;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private String email;

    @Min(value = 1919) @Max(value = 2015)
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;

    @NotNull(message = "개인정보 동의는 필수 입력항목입니다.")
    @AssertTrue(message = "개인정보 동의는 필수 동의항목입니다.")
    private Boolean isPrivacyConsent;
}
