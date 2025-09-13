package com.tnovel.demo.service.user;

import com.tnovel.demo.controller.user.dto.UserCreateRequestDto;
import com.tnovel.demo.controller.user.dto.UserResponseDto;
import com.tnovel.demo.exception.CustomException;
import com.tnovel.demo.exception.ExceptionType;
import com.tnovel.demo.repository.user.UserRepository;
import com.tnovel.demo.repository.user.entity.Following;
import com.tnovel.demo.repository.user.entity.User;
import com.tnovel.demo.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .filter(User::isActivated)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
    }

    public List<User> getFollowingUsers(String username) {
        return this.getLoggedUser().getUsersOneFollow().stream()
                .map(Following::getFollowedUser)
                .filter(User::isActivated)
                .toList();
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
