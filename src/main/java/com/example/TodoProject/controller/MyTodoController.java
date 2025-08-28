package com.example.TodoProject.controller;


import com.example.TodoProject.dto.MyTodoForm;
import com.example.TodoProject.entity.MyTodo;
import com.example.TodoProject.entity.User;
import com.example.TodoProject.repository.MytodoRepository;
import com.example.TodoProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyTodoController {

    private final UserRepository userRepository;

    @GetMapping("/mytodo")
    public String myTodo(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, Model model) {
        // 1. 로그인 사용자 가져오기
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        // 2. 로그인 사용자 기준 Todo 조회
        List<MyTodo> mytodoList = mytodoRepository.findByUser(user);

        // 3. 모델에 등록
        model.addAttribute("user", user);
        model.addAttribute("mytodo_List", mytodoList);

        // 4. 뷰 반환
        return "myPage/index";
    }


    @Autowired // 스프링 부트가 미리 생성해 놓은 리파지터리 객체 주입(DI)
    private MytodoRepository mytodoRepository;

    @GetMapping("/mytodo/new")
    public String newTodoForm(){
        return "myPage/new";
    }

    @PostMapping("/mytodo/create")
    public String createTodo(MyTodoForm form,
                             @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails){
        log.info(form.toString());
        // System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        MyTodo todo = form.toEntity(user);
        log.info(todo.toString());
        // System.out.println(todo.toString());
        // 2. 리파지터리로 엔티티를 DB에 저장
        MyTodo mysaved = mytodoRepository.save(todo);
        log.info(mysaved.toString());
        // System.out.println(saved.toString());
        return "redirect:/mytodo/" + mysaved.getId();
    }

    @GetMapping("/mytodo/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable Long id, Model model){
        log.info("id = " + id);
        // 1. id를 조회해 데이터 가져오기
        MyTodo todoEntity = mytodoRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("mytodo", todoEntity);
        // 3. 뷰 페이지 반환하기
        return "myPage/show";
    }

    @GetMapping("/mytodo/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 가져오기
        MyTodo todoEntity = mytodoRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("mytodo", todoEntity);
        // 뷰 페이지 설정하기
        return "myPage/edit";
    }

    @PostMapping("/mytodo/update")
    public String update(MyTodoForm form,
                         @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        log.info(form.toString());
        // 1. DTO를 엔티팅로 변환하기
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        MyTodo todoEntity = form.toEntity(user);
        log.info(todoEntity.toString());
        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        MyTodo mytarget = mytodoRepository.findById(todoEntity.getId()).orElse(null);
        // 2-2. 기존 데이터 값을 갱신하기
        if (mytarget != null) {
            mytodoRepository.save(todoEntity);
        }
        // 3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/mytodo/" + todoEntity.getId();
    }

    @GetMapping("/mytodo/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!");
        // 1. 삭제할 대상 가져오기
        MyTodo mytarget = mytodoRepository.findById(id).orElse(null);
        log.info(mytarget.toString());
        // 2. 대상 엔티티 삭제하기
        if (mytarget != null) {
            mytodoRepository.delete(mytarget);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        // 3. 결과 페이지로 리다이렉트하기
        return "redirect:/mytodo";
    }
}
