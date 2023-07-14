package com.neodohae_spring_boot.neodohae_spring_boot.service;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;

import java.util.List;

public interface TodoService {
    List<TodoDto> createTodo(TodoDto todoDto);
    List<TodoDto> getTodosByRoomId(Integer roomId);
    List<TodoDto> getTodosByUserId(Integer userId);
    TodoDto getTodoById(Integer id);
    TodoDto updateTodo(TodoDto todoDto, Integer id);
    List<TodoDto> updateTodos(TodoDto todoDto, Integer id);
    void deleteTodo(Integer id);
    void deleteTodos(Integer id);
}
