package com.neodohae_spring_boot.neodohae_spring_boot.service.impl;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoResponse;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.ResourceNotFoundException;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.TodoAPIException;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo.RepeatType;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Room;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import com.neodohae_spring_boot.neodohae_spring_boot.model.User;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.RoomRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.TodoRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.UserRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private ModelMapper mapper;

    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository, RoomRepository roomRepository, ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    // convert DTO to Entity
    private Todo mapToModel(TodoDto todoDto) {
        return mapper.map(todoDto, Todo.class);
    }

    // convert Entity into DTO
    private TodoDto mapToDTO(Todo todo) {
        return mapper.map(todo, TodoDto.class);
    }

    // Room, User, Todo의 유효성을 확인
    private Todo validateTodoOwnership(Integer roomId, Integer userId, Integer id) {
        // retrieve room entity by id
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // retrieve user entity by id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        // check if the todo belongs to the room and user
        if (!todo.getRoom().getId().equals(room.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo does not belong to the room");
        }
        else if (!todo.getUser().getId().equals(user.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo does not belong to the user");
        }

        return todo;
    }

    private List<Todo> createSingleTodo(Room room, User user, TodoDto todoDto) {
        List<Todo> todos = new ArrayList<>();
        // convert DTO to entity
        Todo todo = mapToModel(todoDto);

        // set room and user to todo entity
        todo.setRoom(room);
        todo.setUser(user);

        todos.add(todo);

        return todos;
    }

    private List<Todo> createDailyTodos(Room room, User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartDateTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndDateTime()); date = date.plusDays(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartDateTime(date);
                todo.setEndDateTime(todoDto.getEndDateTime().plusDays(ChronoUnit.DAYS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setRoom(room);
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createWeeklyTodos(Room room, User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartDateTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndDateTime()); date = date.plusWeeks(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartDateTime(date);
                todo.setEndDateTime(todoDto.getEndDateTime().plusWeeks(ChronoUnit.WEEKS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setRoom(room);
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createMonthlyTodos(Room room, User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartDateTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndDateTime()); date = date.plusMonths(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartDateTime(date);
                todo.setEndDateTime(todoDto.getEndDateTime().plusMonths(ChronoUnit.MONTHS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setRoom(room);
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createYearlyTodos(Room room, User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartDateTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndDateTime()); date = date.plusYears(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartDateTime(date);
                todo.setEndDateTime(todoDto.getEndDateTime().plusYears(ChronoUnit.YEARS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setRoom(room);
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    @Override
    public List<TodoDto> createTodo(Integer roomId, Integer userId, TodoDto todoDto) {

        // retrieve room entity by id
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // retrieve user entity by id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        // check if the user belongs to the room
        if (!user.getRoom().getId().equals(room.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The user does not belong to the room");
        }

        // check if todo has a valid date range
        if (todoDto.getStartDateTime().isAfter(todoDto.getEndDateTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getRepeatEndDateTime() == null) todoDto.setEndDateTime(LocalDateTime.parse("2030-01-01T12:00:00"));

        Todo pTodo = mapToModel(todoDto);
        pTodo.setRoom(room);
        pTodo.setUser(user);
        Todo parentTodo = todoRepository.save(pTodo);

        List<Todo> newTodos = new ArrayList<>();

        switch (todoDto.getRepeatType()) {
            case "DAILY":
                newTodos = createDailyTodos(room, user, todoDto, parentTodo);
                break;
            case "WEEKLY":
                newTodos = createWeeklyTodos(room, user, todoDto, parentTodo);
                break;
            case "MONTHLY":
                newTodos = createMonthlyTodos(room, user, todoDto, parentTodo);
                break;
            case "YEARLY":
                newTodos = createYearlyTodos(room, user, todoDto, parentTodo);
                break;
            default:
                newTodos.add(parentTodo);
                break;
        }

        // convert list of todos to list of todo dtos
        return newTodos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());
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

    public List<TodoDto> getTodosByRoomId(Integer roomId) {
        // see if room exists
        roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // retrieve todos by roomId
        List<Todo> todos = todoRepository.findByRoomId(roomId);

        // convert list of todos to list of todo dtos
        return todos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());
    }

    public List<TodoDto> getTodosByRoomIdAndUserId(Integer roomId, Integer userId) {
        // see if room exists
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // see if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        // check if the user belongs to the room
        if (!user.getRoom().getId().equals(room.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The user does not belong to the room");
        }

        // retrieve todos by roomId
        List<Todo> todos = todoRepository.findByRoomIdAndUserId(roomId, userId);

        // convert list of todos to list of todo dtos
        return todos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());
    }

    @Override
    public TodoDto getTodoById(Integer roomId, Integer userId, Integer id) {

        Todo todo = validateTodoOwnership(roomId, userId, id);

        return mapToDTO(todo);
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Integer roomId, Integer userId, Integer id) {

        Todo todo = validateTodoOwnership(roomId, userId, id);

        // update todo using todoDto
        if (todoDto.getStartDateTime() != null) todo.setStartDateTime(todoDto.getStartDateTime());
        if (todoDto.getEndDateTime() != null) todo.setEndDateTime(todoDto.getEndDateTime());
        // check if todo has a valid date range
        if (todo.getStartDateTime().isAfter(todo.getEndDateTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getTitle() != null) todo.setTitle(todoDto.getTitle());
        if (todoDto.getDescription() != null) todo.setDescription(todoDto.getDescription());
        if (todoDto.getStatus() != null) todo.setStatus(Todo.Status.valueOf(todoDto.getStatus()));
        todo.setRepeatType(RepeatType.NONE);
        todo.setRepeatGroupId(null);

        // save updated todo
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDTO(updatedTodo);
    }

    @Override
    public List<TodoDto> updateTodosByRepeatId(TodoDto todoDto, Integer roomId, Integer userId, Integer id) {

        // retrieve room entity by id
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // retrieve user entity by id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        // check if the todo belongs to the room and user
        if (!todo.getRoom().getId().equals(room.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo does not belong to the room");
        }
        else if (!todo.getUser().getId().equals(user.getId())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo does not belong to the user");
        }

        // 변경되지 않은 필드는 todo 필드값을 todoDto에 넣어줌
        if (todoDto.getStartDateTime() == null) todoDto.setStartDateTime(todo.getStartDateTime());
        if (todoDto.getEndDateTime() == null) todoDto.setEndDateTime(todo.getEndDateTime());
        // check if todo has a valid date range
        if (todoDto.getStartDateTime().isAfter(todoDto.getEndDateTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getTitle() == null) todoDto.setTitle(todo.getTitle());
        if (todoDto.getDescription() == null) todoDto.setDescription(todo.getDescription());
        // 확인
        if (todoDto.getStatus() == null) {
            System.out.println("TODODTO NULL");
            todoDto.setStatus(todo.getStatus().toString());
        }
        else System.out.println(todoDto.getStatus());

        if (todoDto.getRepeatEndDateTime() == null) todoDto.setRepeatEndDateTime(todo.getRepeatEndDateTime());
        if (todoDto.getRepeatType() == null) todoDto.setRepeatType(todo.getRepeatType().toString());

        if(!todo.getRepeatType().equals(RepeatType.NONE)) {
            Integer repeatGroupId = todo.getId();
            if (todo.getRepeatGroupId() != null) repeatGroupId = todo.getRepeatGroupId();
            List<Todo> oldTodos = todoRepository.findByRepeatGroupId(repeatGroupId);
            for(Todo t : oldTodos) {
                if(t.getStartDateTime().isAfter(todo.getStartDateTime()))
                    todoRepository.delete(t);
            }
        }
        todoRepository.delete(todo);

        Todo pTodo = mapToModel(todoDto);
        pTodo.setRoom(room);
        pTodo.setUser(user);
        Todo parentTodo = todoRepository.save(pTodo);

        List<Todo> newTodos = new ArrayList<>();

        switch (todoDto.getRepeatType()) {
            case "DAILY":
                newTodos = createDailyTodos(room, user, todoDto, parentTodo);
                break;
            case "WEEKLY":
                newTodos = createWeeklyTodos(room, user, todoDto, parentTodo);
                break;
            case "MONTHLY":
                newTodos = createMonthlyTodos(room, user, todoDto, parentTodo);
                break;
            case "YEARLY":
                newTodos = createYearlyTodos(room, user, todoDto, parentTodo);
                break;
            default:
                newTodos.add(parentTodo);
                break;
        }

        // convert list of todos to list of todo dtos
        return newTodos.stream().map(newTodo -> mapToDTO(newTodo)).collect(Collectors.toList());
    }

    @Override
    public TodoDto updateTodoStatus(TodoDto todoDto, Integer roomId, Integer userId, Integer id) {
        Todo todo = validateTodoOwnership(roomId, userId, id);
        todo.setStatus(Todo.Status.valueOf(todoDto.getStatus()));
        // save updated todo to DB
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDTO(updatedTodo);
    }

    @Override
    public void deleteTodo(Integer roomId, Integer userId, Integer id) {

        Todo todo = validateTodoOwnership(roomId, userId, id);

        todoRepository.delete(todo);
    }

    @Override
    public void deleteTodosByRepeatId(Integer roomId, Integer userId, Integer id) {

        Todo todo = validateTodoOwnership(roomId, userId, id);

        if(todo.getRepeatType().equals(RepeatType.NONE)) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo is not repeating");
        }
        Integer repeatGroupId = todo.getId();
        if (todo.getRepeatGroupId() != null) repeatGroupId = todo.getRepeatGroupId();
        List<Todo> oldTodos = todoRepository.findByRepeatGroupId(repeatGroupId);
        for(Todo t : oldTodos) {
            if(t.getStartDateTime().isAfter(todo.getStartDateTime()))
                todoRepository.delete(t);
        }
        todoRepository.delete(todo);
    }

}
