package com.neodohae_spring_boot.neodohae_spring_boot.controller;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // create todo
    @PostMapping("/todos")
    public ResponseEntity<List<TodoDto>> createTodo(@Valid @RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.createTodo(todoDto), HttpStatus.CREATED);
    }

    // get all todos by room id
    @GetMapping("/todos/room/{roomId}")
    public ResponseEntity<List<TodoDto>> getTodosByRoomId(@PathVariable Integer roomId) {
        return ResponseEntity.ok(todoService.getTodosByRoomId(roomId));
    }

    // get all todos by user id
    // TODO: assignedId 기반으로 todo 호출 필요
    @GetMapping("/todos/user/{userId}")
    public ResponseEntity<List<TodoDto>> getTodosByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(todoService.getTodosByUserId(userId));
    }

    // get todo by id
    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Integer id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    // update single todo by id
    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodo(todoDto, id));
    }

    // update todo and all future repeating todos
    @PutMapping("/todos/{id}/all")
    public ResponseEntity<List<TodoDto>> updateTodos(@RequestBody TodoDto todoDto, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodos(todoDto, id));
    }

    // delete single todo by id
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo entity deleted successfully!");
    }

    // delete todo and all future repeating todos
    @DeleteMapping("/todos/{id}/all")
    public ResponseEntity<String> deleteTodos(@PathVariable Integer id) {
        todoService.deleteTodos(id);
        return ResponseEntity.ok("The todo and all future todos deleted successfully!");
    }

}
