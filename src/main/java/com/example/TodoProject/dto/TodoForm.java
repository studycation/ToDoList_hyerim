package com.example.TodoProject.dto;

import com.example.TodoProject.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
public class TodoForm {
    private Long id;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // HTML <input type="date">의 기본 형식
    private LocalDate date;
    private String content;



    public Todo toEntity() {
        return new Todo(id, title, date, content);
    }
}
