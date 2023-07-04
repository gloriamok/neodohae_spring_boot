package com.neodohae_spring_boot.neodohae_spring_boot.controller;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.neodohae_spring_boot.neodohae_spring_boot.utils.AppConstants.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // create todo
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.createTodo(todoDto), HttpStatus.CREATED);
    }

    // get all todos
    @GetMapping
    public ResponseEntity<TodoResponse> getAllTodos(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(todoService.getAllTodos(pageNo, pageSize, sortBy, sortDir));
    }

    // get todo by id
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    // update todo
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@Valid @RequestBody TodoDto todoDto, @PathVariable Long id) {
        return ResponseEntity.ok(todoService.updateTodo(todoDto, id));
    }

    // delete todo by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.ok("Todo entity deleted successfully!");
    }
}
