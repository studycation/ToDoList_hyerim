package com.example.TodoProject.service;

import com.example.TodoProject.dto.TodoForm;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoServiceTest {
    @Autowired
    TodoService todoService;

    @Test
    void index() {
        // 1. 예상 데이터
        Todo a = new Todo(1L, "가가가가", LocalDate.parse("2025-08-18"), "1111");
        Todo b = new Todo(2L, "나나나나", LocalDate.parse("2025-08-19"), "2222");
        Todo c = new Todo(3L, "다다다다", LocalDate.parse("2025-08-20"), "3333");
        List<Todo> expected = new ArrayList<Todo>(Arrays.asList(a, b, c));
        // 2. 실제 데이터
        List<Todo> todolist = todoService.index();
        // 3. 비교 및 검증
        assertEquals(expected.toString(), todolist.toString());
    }

    @Test
    void show_성공_존재하는_id_입력() {
        // 1. 예상 데이터
        Long id = 1L;
        Todo expected = new Todo(id, "가가가가", LocalDate.parse("2025-08-18"), "1111");
        // 2. 실제 데이터
        Todo todo = todoService.show(id);
        // 3. 비교 및 검증
        assertEquals(expected.toString(), todo.toString());
    }
    @Test
    void show_실패_존재하지_않는_id_입력() {
        // 1. 예상 데이터
        Long id = -1L;
        Todo expected = null;
        // 2. 실제 데이터
        Todo todo = todoService.show(id);
        // 3. 비교 및 검증
        assertEquals(expected, todo);
    }

    @Test
    @Transactional
    void create_성공_title과_content만_있는_dto_입력() {
        // 1. 예상 데이터
        String title = "라라라라";
        LocalDate date = LocalDate.parse("2025-08-08");
        String content = "4444";
        TodoForm dto = new TodoForm(null, title, date, content);
        Todo expected = new Todo(4L, title, date, content);
        // 2. 실제 데이터
        Todo todo = todoService.create(dto);
        // 3. 비교 및 검증
        assertEquals(expected.toString(), todo.toString());
    }
    @Test
    @Transactional
    void create_실패_id가_포함된_dto_입력() {
        // 1. 예상 데이터
        Long id = 4L;
        String title = "라라라라";
        LocalDate date = LocalDate.parse("2025-08-08");
        String content = "4444";
        TodoForm dto = new TodoForm(id, title, date, content);
        Todo expected = null;
        // 2. 실제 데이터
        Todo todo = todoService.create(dto);
        // 3. 비교 킻 검증
        assertEquals(expected, todo);
    }
}