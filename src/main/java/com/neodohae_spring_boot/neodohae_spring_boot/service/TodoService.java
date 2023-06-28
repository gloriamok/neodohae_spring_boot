package com.neodohae_spring_boot.neodohae_spring_boot.service;

import com.neodohae_spring_boot.neodohae_spring_boot.dtos.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto createTodo(TodoDto todoDto);
    List<TodoDto> getAllTodos();
    TodoDto getTodoById(Long id);
    TodoDto updateTodo(TodoDto todoDto, Long id);
    void deleteTodoById(Long id);
}
