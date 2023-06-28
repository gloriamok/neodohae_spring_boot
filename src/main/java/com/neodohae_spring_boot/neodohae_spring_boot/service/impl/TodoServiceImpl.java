package com.neodohae_spring_boot.neodohae_spring_boot.service.impl;

import com.neodohae_spring_boot.neodohae_spring_boot.dtos.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.ResourceNotFoundException;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.TodoRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // convert DTO to Entity
    private Todo mapToModel(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        return todo;
    }

    // convert Entity into DTO
    private TodoDto mapToDTO(Todo todo) {
        TodoDto todoDto = new TodoDto();
        todoDto.setId(todo.getId());
        todoDto.setTitle(todo.getTitle());
        todoDto.setDescription(todo.getDescription());
        return todoDto;
    }

    @Override
    public TodoDto createTodo(TodoDto todoDto) {
        // convert DTO to entity
        Todo todo = mapToModel(todoDto);
        // save Post entity into database
        Todo newTodo = todoRepository.save(todo);
        // convert entity to DTO
        return mapToDTO(newTodo);
    }

    @Override
    public List<TodoDto> getAllTodos() {
        // returns list of Todo Entity
        List<Todo> todos = todoRepository.findAll();
        // convert list of Entity to list of DTO
        return todos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());
    }

    @Override
    public TodoDto getTodoById(Long id) {
        // get todo by id from the database
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
        return mapToDTO(todo);
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {
        // get todo by id from the database
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));

        // update todo using todoDto
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());

        // save updated todo
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDTO(updatedTodo);
    }

    @Override
    public void deleteTodoById(Long id) {
        // get todo by id from the database
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
        todoRepository.delete(todo);
    }


}
