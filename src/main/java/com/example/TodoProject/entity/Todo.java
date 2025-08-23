package com.example.TodoProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private LocalDate date;
    @Column
    private String content;

    public void patch(Todo todo) {
        if(todo.title != null)
            this.title = todo.title;
        if(todo.date  != null)
            this.date = todo.date;
        if(todo.content!= null)
            this.content = todo.content;
    }
}
