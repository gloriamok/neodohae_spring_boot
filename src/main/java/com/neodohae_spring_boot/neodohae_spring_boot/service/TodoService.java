package com.neodohae_spring_boot.neodohae_spring_boot.service;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoDto createTodo(TodoDto todoDto);
    TodoResponse getAllTodos(int pageNo, int pageSize, String sortBy, String sortDir);
    TodoDto getTodoById(Long id);
    TodoDto updateTodo(TodoDto todoDto, Long id);
    void deleteTodoById(Long id);
}
