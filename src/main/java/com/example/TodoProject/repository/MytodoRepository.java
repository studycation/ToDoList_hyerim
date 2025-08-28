package com.example.TodoProject.repository;

import com.example.TodoProject.entity.MyTodo;
import com.example.TodoProject.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface MytodoRepository extends CrudRepository<MyTodo, Long> {
    @Override
    ArrayList<MyTodo> findAll();
    List<MyTodo> findByUser(User user);
}
