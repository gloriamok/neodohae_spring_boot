package com.neodohae_spring_boot.neodohae_spring_boot.service.impl;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.ResourceNotFoundException;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.TodoRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    private ModelMapper mapper;

    public TodoServiceImpl(TodoRepository todoRepository, ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper = mapper;
    }

    // convert DTO to Entity
    private Todo mapToModel(TodoDto todoDto) {
        Todo todo = mapper.map(todoDto, Todo.class);
        return todo;
    }

    // convert Entity into DTO
    private TodoDto mapToDTO(Todo todo) {
        TodoDto todoDto = mapper.map(todo, TodoDto.class);
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
    public TodoResponse getAllTodos(int pageNo, int pageSize, String sortBy, String sortDir) {
        // set sorting direction : asc or desc
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable Instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // returns page object (page of todos)
        Page<Todo> todos = todoRepository.findAll(pageable);

        // get content for page object
        // to retrieve a list of object from the page object
        List<Todo> listOfTodos = todos.getContent();

        // convert list of Entity to list of DTO
        List<TodoDto> contents = listOfTodos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());

        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setContent(contents);
        todoResponse.setPageNo(todos.getNumber());
        todoResponse.setPageSize(todos.getSize());
        todoResponse.setTotalElements(todos.getTotalElements());
        todoResponse.setTotalPages(todos.getTotalPages());
        todoResponse.setLast(todos.isLast());

        return todoResponse;
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
        todo.setStartDateTime(todoDto.getStartDateTime());
        todo.setEndDateTime(todoDto.getEndDateTime());

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
