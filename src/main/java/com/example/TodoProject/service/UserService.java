package com.example.TodoProject.service;

import com.example.TodoProject.dto.SignupRequest;
import com.example.TodoProject.entity.User;
import com.example.TodoProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .build();

        userRepository.save(user);
    }

    public User registerKakaoUser(String email, String nickname) {
        if (email == null || email.isEmpty()) {
            // 이메일 없으면 임시 이메일 생성
            email = nickname + "@kakao.com"; // 닉네임 기반
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setNickname(nickname);
        newUser.setPassword("1234"); // 임시 비밀번호
        return userRepository.save(newUser);
    }
}
