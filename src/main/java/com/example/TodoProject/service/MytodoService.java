package com.example.TodoProject.service;

import com.example.TodoProject.dto.MyTodoForm;
import com.example.TodoProject.entity.MyTodo;
import com.example.TodoProject.entity.User;
import com.example.TodoProject.repository.MytodoRepository;
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
public class MytodoService {
    @Autowired
    private MytodoRepository mytodoRepository;

    public List<MyTodo> index() {
        return mytodoRepository.findAll();
    }

    public MyTodo show(Long id) {
        return mytodoRepository.findById(id).orElse(null);
    }

    public MyTodo create(MyTodoForm dto, User user) {
        MyTodo todo = dto.toEntity(user);
        if (todo.getId() != null) {
            return null;
        }
        return mytodoRepository.save(todo);
    }

    public MyTodo update(Long id, MyTodoForm dto, User user) {
        // 1. DTO -> 엔티티 변환하기
        MyTodo todo = dto.toEntity(user);
        log.info("id: {}, todo: {}", id, todo.toString());
        // 2. 타깃 조회하기
        MyTodo target = mytodoRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리하기
        if(target == null || id != todo.getId()) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, todoL {}", id, todo.toString());
            return null;
        }
        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(todo);
        MyTodo myupdated = mytodoRepository.save(target);
        return myupdated;
    }

    public MyTodo delete(Long id) {
        // 1. 대상 찾기
        MyTodo mytarget = mytodoRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리하기
        if(mytarget == null) {
            return null;
        }
        // 3. 대상 삭제하기
        mytodoRepository.delete(mytarget);
        return mytarget;
    }

    @Transactional
    public List<MyTodo> createTodolist(List<MyTodoForm> dtos, User user) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<MyTodo> mytodoList = dtos.stream()
                .map(dto -> dto.toEntity(user))  // user 포함
                .collect(Collectors.toList());
        // 2. 엔티티 묶음을 DB에 저장하기
        mytodoList.forEach(todo -> mytodoRepository.save(todo));
        // 3. 강제 예외 발생시키기
        mytodoRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));
        // 4. 결과 값 반환하기
        return mytodoList;
    }
}
