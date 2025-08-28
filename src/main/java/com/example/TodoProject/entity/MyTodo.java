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
public class MyTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private LocalDate date;
    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void patch(MyTodo mytodo) {
        if(mytodo.title != null)
            this.title = mytodo.title;
        if(mytodo.date  != null)
            this.date = mytodo.date;
        if(mytodo.content!= null)
            this.content = mytodo.content;
    }
}
