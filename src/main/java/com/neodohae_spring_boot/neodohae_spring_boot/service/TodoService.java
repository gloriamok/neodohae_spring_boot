package com.neodohae_spring_boot.neodohae_spring_boot.service;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;

import java.util.List;

public interface TodoService {
    List<TodoDto> createTodo(Integer roomId, Integer userId, TodoDto todoDto);
    TodoResponse getAllTodos(int pageNo, int pageSize, String sortBy, String sortDir);
    List<TodoDto> getTodosByRoomId(Integer roomId);
    List<TodoDto> getTodosByRoomIdAndUserId(Integer roomId, Integer userId);
    TodoDto getTodoById(Integer roomId, Integer userId, Integer id);
    TodoDto updateTodo(TodoDto todoDto, Integer roomId, Integer userId, Integer id);
    List<TodoDto> updateTodosByRepeatId(TodoDto todoDto, Integer roomId, Integer userId, Integer id);
    TodoDto updateTodoStatus(TodoDto todoDto, Integer roomId, Integer userId, Integer id);
    void deleteTodo(Integer roomId, Integer userId, Integer id);
    void deleteTodosByRepeatId(Integer roomId, Integer userId, Integer id);
}
