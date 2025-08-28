package com.example.TodoProject.service;

import com.example.TodoProject.dto.SignupRequest;
import com.example.TodoProject.entity.User;
import com.example.TodoProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        // 이메일이 없으면 임시 이메일 생성
        if (email == null || email.isEmpty()) {
            email = "kakao_" + System.currentTimeMillis() + "@todo.com";
        }

        // 이미 가입된 사용자인지 확인
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            // 신규 회원이면 저장
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("kakao_default_password"))
                    .nickname(nickname)
                    .build();

            userRepository.save(user);
        }
        return user;
    }
}
