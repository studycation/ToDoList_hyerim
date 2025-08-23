package com.example.TodoProject.api;

import com.example.TodoProject.dto.TodoForm;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.repository.TodoRepository;
import com.example.TodoProject.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TodoApiController {
    @Autowired
    private TodoService todoService;
    // GET
    @GetMapping("/api/todolist")
    public List<Todo> index() {
        return todoService.index();
    }
    @GetMapping("/api/todolist/{id}")
    public Todo show(@PathVariable Long id) {
        return todoService.show(id);
    }
    // POST
    @PostMapping("/api/todolist")
    public ResponseEntity<Todo> create(@RequestBody TodoForm dto) {
        Todo created = todoService.create(dto);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    // PATCH
    @PatchMapping("/api/todolist/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id,
                       @RequestBody TodoForm dto) {
        Todo updated = todoService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    // DELETE
    @DeleteMapping("/api/todolist/{id}")
    public ResponseEntity<Todo> delete(@PathVariable Long id) {
        Todo deleted = todoService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Todo>> transactionTest
            (@RequestBody List<TodoForm> dtos) {
        List<Todo> createdList = todoService.createTodolist(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
