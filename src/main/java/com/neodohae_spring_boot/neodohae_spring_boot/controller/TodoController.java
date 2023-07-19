package com.neodohae_spring_boot.neodohae_spring_boot.controller;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api")
@Tag(name = "todos")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "새 Todo 생성")
    @ApiResponse(
            responseCode = "201",
            description = "Todo 생성 완료"
    )
    // create todo
    @PostMapping("/todos")
    public ResponseEntity<List<TodoDto>> createTodo(@Valid @RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.createTodo(todoDto), HttpStatus.CREATED);
    }

    @Operation(summary = "roomId로 room에 있는 모든 Todo 조회")
    @ApiResponse(
            responseCode = "200",
            description = "Room에 있는 모든 Todo 목록 반환"
    )
    // get all todos by room id
    @GetMapping("/todos/room/{roomId}")
    public ResponseEntity<List<TodoDto>> getTodosByRoomId(@PathVariable Integer roomId) {
        return ResponseEntity.ok(todoService.getTodosByRoomId(roomId));
    }

    @Operation(summary = "userId로 user가 할당된 모든 Todo 조회")
    @ApiResponse(
            responseCode = "200",
            description = "User가 할당된 모든 Todo 목록 반환"
    )
    // get all todos by user id
    // TODO: assignedId 기반으로 todo 호출 필요
    @GetMapping("/todos/user/{userId}")
    public ResponseEntity<List<TodoDto>> getTodosByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(todoService.getTodosByUserId(userId));
    }

    @Operation(summary = "Id로 Todo 조회")
    @ApiResponse(
            responseCode = "200",
            description = "해당 Id의 Todo 정보 반환"
    )
    // get todo by id
    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Integer id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @Operation(summary = "단일 Todo 수정")
    @ApiResponse(
            responseCode = "200",
            description = "단일 Todo 수정 완료"
    )
    // update single todo by id
    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodo(todoDto, id));
    }

    @Operation(summary = "해당 Todo와 반복되는 미래의 Todo 모두 수정")
    @ApiResponse(
            responseCode = "200",
            description = "해당 Todo와 반복되는 미래의 Todo 모두 수정 완료"
    )
    // update todo and all future repeating todos
    @PutMapping("/todos/{id}/all")
    public ResponseEntity<List<TodoDto>> updateTodos(@RequestBody TodoDto todoDto, @PathVariable Integer id) {
        return ResponseEntity.ok(todoService.updateTodos(todoDto, id));
    }

    @Operation(summary = "단일 Todo 삭제")
    @ApiResponse(
            responseCode = "200",
            description = "단일 Todo 삭제 완료"
    )
    // delete single todo by id
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo entity deleted successfully!");
    }

    @Operation(summary = "해당 Todo와 반복되는 미래의 Todo 모두 삭제")
    @ApiResponse(
            responseCode = "200",
            description = "해당 Todo와 반복되는 미래의 Todo 모두 삭제 완료"
    )
    // delete todo and all future repeating todos
    @DeleteMapping("/todos/{id}/all")
    public ResponseEntity<String> deleteTodos(@PathVariable Integer id) {
        todoService.deleteTodos(id);
        return ResponseEntity.ok("The todo and all future todos deleted successfully!");
    }

}
