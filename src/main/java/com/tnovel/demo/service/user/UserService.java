package com.tnovel.demo.service.user;

import com.tnovel.demo.controller.user.dto.UserCreateRequestDto;
import com.tnovel.demo.controller.user.dto.PasswordResetDto;
import com.tnovel.demo.controller.user.dto.PasswordResetRequestDto;
import com.tnovel.demo.controller.user.dto.UserResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
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

        return UserResponseDto.of(saved);
    }
}
