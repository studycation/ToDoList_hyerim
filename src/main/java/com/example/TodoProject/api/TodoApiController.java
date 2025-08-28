package com.example.TodoProject.api;

import com.example.TodoProject.dto.TodoForm;
import com.example.TodoProject.entity.Todo;
import com.example.TodoProject.entity.User;
import com.example.TodoProject.repository.TodoRepository;
import com.example.TodoProject.repository.UserRepository;
import com.example.TodoProject.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TodoApiController {

    private final UserRepository userRepository;

    @Autowired
    private TodoService todoService;

    public TodoApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public ResponseEntity<Todo> create(@RequestBody TodoForm dto, @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Todo created = todoService.create(dto, user);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    // PATCH
    @PatchMapping("/api/todolist/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id,
                       @RequestBody TodoForm dto, @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        Todo updated = todoService.update(id, dto, user);
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
            (@RequestBody List<TodoForm> dtos, @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        List<Todo> createdList = todoService.createTodolist(dtos, user);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
