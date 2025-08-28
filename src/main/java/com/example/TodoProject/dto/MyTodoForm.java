package com.example.TodoProject.dto;

import com.example.TodoProject.entity.MyTodo;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.entity.User;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
public class MyTodoForm {
    private Long id;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // HTML <input type="date">의 기본 형식
    private LocalDate date;
    private String content;

    public MyTodo toEntity(User user) {
        return new MyTodo(id, title, date, content, user);
    }
}
