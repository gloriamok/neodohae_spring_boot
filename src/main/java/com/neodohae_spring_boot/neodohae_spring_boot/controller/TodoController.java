package com.neodohae_spring_boot.neodohae_spring_boot.controller;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.neodohae_spring_boot.neodohae_spring_boot.utils.AppConstants.*;

@RestController
@RequestMapping("/api")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // create todo
    @PostMapping("/rooms/{roomId}/users/{userId}/todos")
    public ResponseEntity<List<TodoDto>> createTodo(@PathVariable Integer roomId, @PathVariable Integer userId, @Valid @RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.createTodo(roomId, userId, todoDto), HttpStatus.CREATED);
    }

    // get all todos
    @GetMapping("/todos")
    public ResponseEntity<TodoResponse> getAllTodos(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(todoService.getAllTodos(pageNo, pageSize, sortBy, sortDir));
    }

    // get all todos by room id
    @GetMapping("/rooms/{roomId}/todos")
    public ResponseEntity<List<TodoDto>> getTodosByRoomId(@PathVariable Integer roomId) {
        return ResponseEntity.ok(todoService.getTodosByRoomId(roomId));
    }

    // get all todos by room id and user id
    @GetMapping("/rooms/{roomId}/users/{userId}/todos")
    public ResponseEntity<List<TodoDto>> getTodosByRoomIdAndUserId(@PathVariable Integer roomId, @PathVariable Integer userId) {
        return ResponseEntity.ok(todoService.getTodosByRoomIdAndUserId(roomId, userId));
    }

    // get todo by id
    @GetMapping("/rooms/{roomId}/users/{userId}/todos/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Integer roomId, @PathVariable Integer userId, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.getTodoById(roomId, userId, id));
    }

    // update single todo by id
    @PutMapping("/rooms/{roomId}/users/{userId}/todos/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable Integer roomId, @PathVariable Integer userId, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodo(todoDto, roomId, userId, id));
    }

    // update todos by RepeatId
    @PutMapping("/rooms/{roomId}/users/{userId}/todos/{id}/all")
    public ResponseEntity<List<TodoDto>> updateTodosByRepeatId(@RequestBody TodoDto todoDto, @PathVariable Integer roomId, @PathVariable Integer userId, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodosByRepeatId(todoDto, roomId, userId, id));
    }

    // delete single todo by id
    @DeleteMapping("/rooms/{roomId}/users/{userId}/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer roomId, @PathVariable Integer userId, @PathVariable Integer id) {
        todoService.deleteTodo(roomId, userId, id);
        return ResponseEntity.ok("Todo entity deleted successfully!");
    }

    // delete todos by RepeatId
    @DeleteMapping("/rooms/{roomId}/users/{userId}/todos/{id}/all")
    public ResponseEntity<String> deleteTodosByRepeatId(@PathVariable Integer roomId, @PathVariable Integer userId, @PathVariable Integer id) {
        todoService.deleteTodosByRepeatId(roomId, userId, id);
        return ResponseEntity.ok("The todo and all future todos deleted successfully!");
    }

}
