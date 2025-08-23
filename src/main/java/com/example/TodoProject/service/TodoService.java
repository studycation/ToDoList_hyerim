package com.example.TodoProject.service;

import com.example.TodoProject.dto.TodoForm;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public Todo show(Long id) {
        return todoRepository.findById(id).orElse(null);
    }

    public Todo create(TodoForm dto) {
        Todo todo = dto.toEntity();
        if (todo.getId() != null) {
            return null;
        }
        return todoRepository.save(todo);
    }

    public Todo update(Long id, TodoForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Todo todo = dto.toEntity();
        log.info("id: {}, todo: {}", id, todo.toString());
        // 2. 타깃 조회하기
        Todo target = todoRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리하기
        if(target == null || id != todo.getId()) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, todoL {}", id, todo.toString());
            return null;
        }
        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(todo);
        Todo updated = todoRepository.save(target);
        return updated;
    }

    public Todo delete(Long id) {
        // 1. 대상 찾기
        Todo target = todoRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리하기
        if(target == null) {
            return null;
        }
        // 3. 대상 삭제하기
        todoRepository.delete(target);
        return target;
    }

    @Transactional
    public List<Todo> createTodolist(List<TodoForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Todo> todoList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        // 2. 엔티티 묶음을 DB에 저장하기
        todoList.stream()
                .forEach(todo -> todoRepository.save(todo));
        // 3. 강제 예외 발생시키기
        todoRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));
        // 4. 결과 값 반환하기
        return todoList;
    }
}
