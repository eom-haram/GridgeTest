package com.tnovel.demo.service.user;

import com.tnovel.demo.controller.user.dto.UserCreateRequestDto;
import com.tnovel.demo.controller.user.dto.UserResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.DataStatus;
import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.security.SecurityUtil;
import com.tnovel.demo.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .filter(User::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
    }

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException(ExceptionType.USERNAME_DUPLICATION);
        }

        LocalDate birthdate = LocalDate.of(request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());

        User user = User.create(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(), request.getPhNum(), request.getEmail(), birthdate);
        User saved = userRepository.save(user);

        return UserResponseDto.from(saved);
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    public void checkPrivacyConsent() {
        List<User> users = userRepository.findByDataStatusAndPrivacyConsentDateBefore(
                DataStatus.ACTIVATED,
                LocalDateTime.now().minusYears(1)
        );

        String subject = "개인정보처리 재동의";
        String content = """
                개인정보보호법에 따라 1년마다 개인정보처리동의가 필요합니다.\n
                돌아오는 1년도 Tnovel과 함께 해주세요!
                """;
        for (User user:users) {
            emailService.sendEmail(user.getEmail(), subject, content);
        }
    }

    @Transactional
    public User internalFindById(Integer id) {
        return userRepository.findById(id)
                .filter(User::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
    }

    public User getLoggedUser() {
        return (User) this.loadUserByUsername(SecurityUtil.getCurrentUsername());
    }
}
