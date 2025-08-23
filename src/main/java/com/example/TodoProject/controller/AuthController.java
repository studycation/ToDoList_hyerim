package com.example.TodoProject.controller;

import com.example.TodoProject.dto.SignupRequest;
import com.example.TodoProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage(SignupRequest form, Model model) {
        return "auth/signup";
    }

    // 회원가입 처리
    @PostMapping("/auth/signup")
    public String signup(@Valid SignupRequest form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "입력값을 확인해주세요.");
            return "auth/signup";
        }
        try {
            userService.register(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/signup";
        }
        return "redirect:/login?registered";
    }

    // 로그인 페이지 (실제 인증 처리는 Spring Security가 담당)
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}
