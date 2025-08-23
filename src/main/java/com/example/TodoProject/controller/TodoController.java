package com.example.TodoProject.controller;

import com.example.TodoProject.dto.TodoForm;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.repository.TodoRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TodoController {
    @Autowired // 스프링 부트가 미리 생성해 놓은 리파지터리 객체 주입(DI)
    private TodoRepository todoRepository;

    @GetMapping("/todolist/new")
    public String newTodoForm(){
        return "todolist/new";
    }

    @PostMapping("/todolist/create")
    public String createTodo(TodoForm form){
        log.info(form.toString());
        // System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        Todo todo = form.toEntity();
        log.info(todo.toString());
        // System.out.println(todo.toString());
        // 2. 리파지터리로 엔티티를 DB에 저장
        Todo saved = todoRepository.save(todo);
        log.info(saved.toString());
        // System.out.println(saved.toString());
        return "redirect:/todolist/" + saved.getId();
    }

    @GetMapping("/todolist/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable Long id, Model model){
        log.info("id = " + id);
        // 1. id를 조회해 데이터 가져오기
        Todo todoEntity = todoRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("todo", todoEntity);
        // 3. 뷰 페이지 반환하기
        return "todolist/show";
    }

    @GetMapping("/todolist")
    public String index(Model model){
        // 1. 모든 데이터 가져오기
        ArrayList<Todo> todoEntityList = todoRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("todo_List", todoEntityList);
        // 3. 뷰 페이지 설정하기
        return "todoList/index";
    }

    @GetMapping("/todolist/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 가져오기
        Todo todoEntity = todoRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("todo", todoEntity);
        // 뷰 페이지 설정하기
        return "todolist/edit";
    }

    @PostMapping("/todolist/update")
    public String update(TodoForm form) {
        log.info(form.toString());
        // 1. DTO를 엔티팅로 변환하기
        Todo todoEntity = form.toEntity();
        log.info(todoEntity.toString());
        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        Todo target = todoRepository.findById(todoEntity.getId()).orElse(null);
        // 2-2. 기존 데이터 값을 갱신하기
        if (target != null) {
            todoRepository.save(todoEntity);
        }
        // 3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/todolist/" + todoEntity.getId();
    }

    @GetMapping("/todolist/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!");
        // 1. 삭제할 대상 가져오기
        Todo target = todoRepository.findById(id).orElse(null);
        log.info(target.toString());
        // 2. 대상 엔티티 삭제하기
        if (target != null) {
            todoRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        // 3. 결과 페이지로 리다이렉트하기
        return "redirect:/todolist";
    }
}
