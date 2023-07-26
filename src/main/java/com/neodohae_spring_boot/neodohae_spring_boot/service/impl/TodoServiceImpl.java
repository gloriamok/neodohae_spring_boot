package com.neodohae_spring_boot.neodohae_spring_boot.service.impl;

import com.neodohae_spring_boot.neodohae_spring_boot.dto.TodoDto;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.ResourceNotFoundException;
import com.neodohae_spring_boot.neodohae_spring_boot.exception.TodoAPIException;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo.RepeatType;
import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import com.neodohae_spring_boot.neodohae_spring_boot.model.User;
import com.neodohae_spring_boot.neodohae_spring_boot.model.TodoUserMap;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.RoomRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.TodoRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.UserRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.repository.TodoUserMapRepository;
import com.neodohae_spring_boot.neodohae_spring_boot.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    private TodoUserMapRepository todoUserMapRepository;
    private ModelMapper mapper;

    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository, RoomRepository roomRepository, TodoUserMapRepository todoUserMapRepository, ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.todoUserMapRepository = todoUserMapRepository;
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

    private List<Todo> createDailyTodos(User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndTime()); date = date.plusDays(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartTime(date);
                todo.setEndTime(todoDto.getEndTime().plusDays(ChronoUnit.DAYS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createWeeklyTodos(User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndTime()); date = date.plusWeeks(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartTime(date);
                todo.setEndTime(todoDto.getEndTime().plusWeeks(ChronoUnit.WEEKS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createMonthlyTodos(User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndTime()); date = date.plusMonths(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartTime(date);
                todo.setEndTime(todoDto.getEndTime().plusMonths(ChronoUnit.MONTHS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    private List<Todo> createYearlyTodos(User user, TodoDto todoDto, Todo parentTodo) {
        List<Todo> todos = new ArrayList<>();
        todos.add(parentTodo);
        LocalDateTime start_date = todoDto.getStartTime();
        for(LocalDateTime date = start_date; !date.isAfter(todoDto.getRepeatEndTime()); date = date.plusYears(1)) {
            if (date != start_date) {
                Todo todo = mapToModel(todoDto);
                todo.setStartTime(date);
                todo.setEndTime(todoDto.getEndTime().plusYears(ChronoUnit.YEARS.between(start_date, date)));
                todo.setStatus(Todo.Status.TODO);
                todo.setRepeatGroupId(parentTodo.getId());
                todo.setUser(user);
                todos.add(todo);
            }
        }
        // save todo entities into database and return them
        return todoRepository.saveAll(todos);
    }

    @Override
    public List<TodoDto> createTodo(TodoDto todoDto) {

        // retrieve user entity by id
        User user = userRepository.findById(todoDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User","id",todoDto.getUserId()));

        // check if todo has a valid date range
        if (todoDto.getStartTime().isAfter(todoDto.getEndTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getRepeatEndTime() == null) todoDto.setRepeatEndTime(LocalDateTime.parse("2030-01-01T12:00:00"));
        if (todoDto.getStatus() == null) todoDto.setStatus("TODO");
        if (todoDto.getRepeatType() == null) todoDto.setRepeatType("NONE");

        Todo pTodo = mapToModel(todoDto);
        pTodo.setUser(user);
        Todo parentTodo = todoRepository.save(pTodo);

        List<Todo> newTodos = new ArrayList<>();

        switch (todoDto.getRepeatType()) {
            case "DAILY":
                newTodos = createDailyTodos(user, todoDto, parentTodo);
                break;
            case "WEEKLY":
                newTodos = createWeeklyTodos(user, todoDto, parentTodo);
                break;
            case "MONTHLY":
                newTodos = createMonthlyTodos(user, todoDto, parentTodo);
                break;
            case "YEARLY":
                newTodos = createYearlyTodos(user, todoDto, parentTodo);
                break;
            default:
                newTodos.add(parentTodo);
                break;
        }

        // convert list of todos to list of todo dtos
        List<TodoDto> newTodoDtos = newTodos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());

        // random or not
        if(todoDto.getRandomUsersNum() == null || todoDto.getRandomUsersNum().equals(0)) {
            // assignedUserIds -> todoId - userId mapping
            if (todoDto.getAssignedUserIds() == null || todoDto.getAssignedUserIds().equals("")) {
                todoDto.setAssignedUserIds(Collections.singleton(todoDto.getUserId()));
            }
            int i = 0;
            for(Todo todo : newTodos) {
                List<TodoUserMap> todoUserMaps = new ArrayList<>();
                for(Integer uid : todoDto.getAssignedUserIds()) {
                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(todo);
                    // retrieve user entity by id
                    User assignedUser = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User","id",uid));
                    // check if the assignedUser belongs to the room
                    if (!assignedUser.getRoom().getId().equals(user.getRoom().getId())) {
                        throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The assigned user does not belong to the room");
                    }
                    todoUserMap.setUser(assignedUser);
                    todoUserMaps.add(todoUserMap);
                }
                List<TodoUserMap> newTodoUserMaps = todoUserMapRepository.saveAll(todoUserMaps);

                Set<Integer> newAssignedUserIds = new HashSet<>();

                for(TodoUserMap todoUserMap : newTodoUserMaps) {
                    newAssignedUserIds.add(todoUserMap.getUser().getId());
                }
                newTodoDtos.get(i).setAssignedUserIds(newAssignedUserIds);
                i++;
            }
        }
        else if (todoDto.getRandomUsersNum() != null && !todoDto.getRandomUsersNum().equals(0)) {
            // assign user ramdomly
            // numRandomUsers & random -> todoId - userId mapping
            Integer randomUsersNum = todoDto.getRandomUsersNum();

            int i=0;
            for(Todo todo : newTodos) {
                List<User> users = userRepository.findByRoomId(user.getRoom().getId());
                List<TodoUserMap> todoUserMaps = new ArrayList<>();
                for (int j=1;j<=randomUsersNum;j++) {
                    Random random = new Random();
                    int randomIdx = random.nextInt(users.size());
                    User randomUser = users.get(randomIdx);
                    // 한 번 선택한 user는 다시 선택 불가능하게 users에서 제거
                    users.remove(randomIdx);

                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(todo);
                    todoUserMap.setUser(randomUser);
                    todoUserMaps.add(todoUserMap);
                }
                List<TodoUserMap> newTodoUserMaps = todoUserMapRepository.saveAll(todoUserMaps);

                Set<Integer> newAssignedUserIds = new HashSet<>();

                for(TodoUserMap todoUserMap : newTodoUserMaps) {
                    newAssignedUserIds.add(todoUserMap.getUser().getId());
                }
                newTodoDtos.get(i).setAssignedUserIds(newAssignedUserIds);
                newTodoDtos.get(i).setRandomUsersNum(randomUsersNum);
                i++;
            }
        }

        return newTodoDtos;
    }

    public List<TodoDto> getTodosByRoomId(Integer roomId) {
        // see if room exists
        roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));

        // retrieve users by roomId
        List<User> users = userRepository.findByRoomId(roomId);

        // retrieve todos by userId
        List<Todo> todos = new ArrayList<>();
        for(User user : users) {
            todos.addAll(todoRepository.findByUserId(user.getId()));
        }

        // convert list of todos to list of todo dtos
        List<TodoDto> todoDtos = todos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());

        // convert set of todoUserMaps to set of assignedUserIds
        for(TodoDto todoDto : todoDtos) {
            Set<Integer> newAssignedUserIds = new HashSet<>();
            List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByTodoId(todoDto.getId());
            for(TodoUserMap todoUserMap : todoUserMaps) {
                newAssignedUserIds.add(todoUserMap.getUser().getId());
            }
            todoDto.setAssignedUserIds(newAssignedUserIds);
        }
        return todoDtos;
    }

    // TODO: assignedId 기반으로 todo 호출 필요
    public List<TodoDto> getTodosByUserId(Integer userId) {
        // see if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        // retrieve todoUserMaps by userId
        List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByUserId(userId);

        // retrieve todos by todoIds from todoUserMaps
        List<Todo> todos = new ArrayList<>();
        for(TodoUserMap todoUserMap : todoUserMaps) {
            Todo todo = todoRepository.findById(todoUserMap.getTodo().getId()).orElseThrow(() -> new ResourceNotFoundException("Todo","id",todoUserMap.getTodo().getId()));
            todos.add(todo);
        }

        // convert list of todos to list of todo dtos
        List<TodoDto> todoDtos = todos.stream().map(todo -> mapToDTO(todo)).collect(Collectors.toList());

        // convert set of todoUserMaps to set of assignedUserIds
        for(TodoDto todoDto : todoDtos) {
            Set<Integer> newAssignedUserIds = new HashSet<>();
            List<TodoUserMap> newTodoUserMaps = todoUserMapRepository.findByTodoId(todoDto.getId());
            for(TodoUserMap todoUserMap : newTodoUserMaps) {
                newAssignedUserIds.add(todoUserMap.getUser().getId());
            }
            todoDto.setAssignedUserIds(newAssignedUserIds);
        }
        return todoDtos;
    }

    @Override
    public TodoDto getTodoById(Integer id) {

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        TodoDto todoDto = mapToDTO(todo);

        // convert set of todoUserMaps to set of assignedUserIds
        Set<Integer> newAssignedUserIds = new HashSet<>();
        List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByTodoId(id);
        for(TodoUserMap todoUserMap : todoUserMaps) {
            newAssignedUserIds.add(todoUserMap.getUser().getId());
        }
        todoDto.setAssignedUserIds(newAssignedUserIds);

        return todoDto;
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Integer id) {

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        User user = userRepository.findById(todo.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User","id", todo.getUser().getId()));

        // update todo using todoDto
        if (todoDto.getStartTime() != null) todo.setStartTime(todoDto.getStartTime());
        if (todoDto.getEndTime() != null) todo.setEndTime(todoDto.getEndTime());
        // check if todo has a valid date range
        if (todo.getStartTime().isAfter(todo.getEndTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getTitle() != null) todo.setTitle(todoDto.getTitle());
        if (todoDto.getDescription() != null) todo.setDescription(todoDto.getDescription());
        if (todoDto.getStatus() != null) todo.setStatus(Todo.Status.valueOf(todoDto.getStatus()));
        if (todoDto.getRepeatEndTime() != null) todo.setRepeatEndTime(todoDto.getRepeatEndTime());
        if (todoDto.getRepeatType() != null) {
            if (todoDto.getRepeatType().equals("NONE")) {
                todo.setRepeatType(RepeatType.NONE);
                todo.setRepeatGroupId(null);
            }
            else todo.setRepeatType(RepeatType.valueOf(todoDto.getRepeatType()));
        }

        // save updated todo
        Todo updatedTodo = todoRepository.save(todo);

        TodoDto newTodoDto = mapToDTO(updatedTodo);

        // assignedUserIds에 변경사항 있을 시
        if (todoDto.getAssignedUserIds() != null || (todoDto.getRandomUsersNum() != null && !todoDto.getRandomUsersNum().equals(0))) {
            // 기존 todo - user 맵핑 제거
            List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByTodoId(todo.getId());
            todoUserMapRepository.deleteAll(todoUserMaps);

            List<TodoUserMap> newTodoUserMaps = new ArrayList<>();

            if (todoDto.getAssignedUserIds() != null) {
                // AssignedUserIds로 새로운 todo - user 맵핑 생성
                for(Integer uid : todoDto.getAssignedUserIds()) {
                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(todo);
                    // retrieve user entity by id
                    User assignedUser = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User","id",uid));
                    // check if the assignedUser belongs to the room
                    if (!assignedUser.getRoom().getId().equals(user.getRoom().getId())) {
                        throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The assigned user does not belong to the room");
                    }
                    todoUserMap.setUser(assignedUser);
                    newTodoUserMaps.add(todoUserMap);
                }
            }
            else if (todoDto.getRandomUsersNum() != null && !todoDto.getRandomUsersNum().equals(0)) {
                // assign user ramdomly
                // numRandomUsers & random -> todoId - userId mapping
                Integer randomUsersNum = todoDto.getRandomUsersNum();
                List<User> users = userRepository.findByRoomId(user.getRoom().getId());

                for (int j=1;j<=randomUsersNum;j++) {
                    Random random = new Random();
                    int randomIdx = random.nextInt(users.size());
                    User randomUser = users.get(randomIdx);
                    // 한 번 선택한 user는 다시 선택 불가능하게 users에서 제거
                    users.remove(randomIdx);

                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(todo);
                    todoUserMap.setUser(randomUser);
                    newTodoUserMaps.add(todoUserMap);
                }
                newTodoDto.setRandomUsersNum(randomUsersNum);
            }
            List<TodoUserMap> updatedTodoUserMaps = todoUserMapRepository.saveAll(newTodoUserMaps);

            Set<Integer> newAssignedUserIds = new HashSet<>();

            for(TodoUserMap todoUserMap : updatedTodoUserMaps) {
                newAssignedUserIds.add(todoUserMap.getUser().getId());
            }
            newTodoDto.setAssignedUserIds(newAssignedUserIds);


        }
        else {
            List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByTodoId(todo.getId());
            Set<Integer> newAssignedUserIds = new HashSet<>();

            for(TodoUserMap todoUserMap : todoUserMaps) {
                newAssignedUserIds.add(todoUserMap.getUser().getId());
            }
            newTodoDto.setAssignedUserIds(newAssignedUserIds);
        }

        return newTodoDto;
    }

    @Override
    public List<TodoDto> updateTodos(TodoDto todoDto, Integer id) {

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        User user = userRepository.findById(todo.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User","id", todo.getUser().getId()));

        // 변경되지 않은 필드는 todo 필드값을 todoDto에 넣어줌
        if (todoDto.getStartTime() == null) todoDto.setStartTime(todo.getStartTime());
        if (todoDto.getEndTime() == null) todoDto.setEndTime(todo.getEndTime());
        // check if todo has a valid date range
        if (todoDto.getStartTime().isAfter(todoDto.getEndTime())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The start date cannot be later than the end date. Please enter a valid date range.");
        }

        if (todoDto.getTitle() == null) todoDto.setTitle(todo.getTitle());
        if (todoDto.getDescription() == null) todoDto.setDescription(todo.getDescription());
        // 확인
        if (todoDto.getStatus() == null) todoDto.setStatus(todo.getStatus().toString());
        else System.out.println(todoDto.getStatus());

        if (todoDto.getRepeatEndTime() == null) todoDto.setRepeatEndTime(todo.getRepeatEndTime());
        if (todoDto.getRepeatType() == null) todoDto.setRepeatType(todo.getRepeatType().toString());

        // todoDto.getAssignedUserIds()와 todoDto.getRandomUsersNum()가 둘 다 null이면
        // 기존 parentTodo의 id에 해당하는 기존 todoUserMap을 가져와서 todoDto에 setAssignedUserIds
        // todo를 DB에서 지우면 해당 toddmap도 삭제되므로 todo를 지우기 전에 가져오기
        if (todoDto.getAssignedUserIds() == null && todoDto.getRandomUsersNum() == null) {
            List<TodoUserMap> todoUserMaps = todoUserMapRepository.findByTodoId(todo.getId());
            Set<Integer> newAssignedUserIds = new HashSet<>();

            for(TodoUserMap todoUserMap : todoUserMaps) {
                newAssignedUserIds.add(todoUserMap.getUser().getId());
            }
            todoDto.setAssignedUserIds(newAssignedUserIds);
        }

        if(!todo.getRepeatType().equals(RepeatType.NONE)) {
            Integer repeatGroupId = todo.getId();
            if (todo.getRepeatGroupId() != null) repeatGroupId = todo.getRepeatGroupId();
            List<Todo> oldTodos = todoRepository.findByRepeatGroupId(repeatGroupId);
            for(Todo t : oldTodos) {
                if(t.getStartTime().isAfter(todo.getStartTime()))
                    todoRepository.delete(t);
            }
        }
        todoRepository.delete(todo);

        Todo pTodo = mapToModel(todoDto);
        pTodo.setUser(user);
        Todo parentTodo = todoRepository.save(pTodo);

        List<Todo> newTodos = new ArrayList<>();

        switch (todoDto.getRepeatType()) {
            case "DAILY":
                newTodos = createDailyTodos(user, todoDto, parentTodo);
                break;
            case "WEEKLY":
                newTodos = createWeeklyTodos(user, todoDto, parentTodo);
                break;
            case "MONTHLY":
                newTodos = createMonthlyTodos(user, todoDto, parentTodo);
                break;
            case "YEARLY":
                newTodos = createYearlyTodos(user, todoDto, parentTodo);
                break;
            default:
                newTodos.add(parentTodo);
                break;
        }

        // convert list of todos to list of todo dtos
        List<TodoDto> newTodoDtos = newTodos.stream().map(newTodo -> mapToDTO(newTodo)).collect(Collectors.toList());


        // random값 없을 때
        if (todoDto.getAssignedUserIds() != null && todoDto.getRandomUsersNum() == null) {
            // assignedUserIds -> todoId - userId mapping
            int i = 0;
            for (Todo newTodo : newTodos) {
                List<TodoUserMap> todoUserMaps = new ArrayList<>();
                for (Integer uid : todoDto.getAssignedUserIds()) {
                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(newTodo);
                    // retrieve user entity by id
                    User assignedUser = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uid));
                    // check if the assignedUser belongs to the room
                    if (!assignedUser.getRoom().getId().equals(user.getRoom().getId())) {
                        throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The assigned user does not belong to the room");
                    }
                    todoUserMap.setUser(assignedUser);
                    todoUserMaps.add(todoUserMap);
                }
                List<TodoUserMap> newTodoUserMaps = todoUserMapRepository.saveAll(todoUserMaps);

                Set<Integer> newAssignedUserIds = new HashSet<>();

                for (TodoUserMap todoUserMap : newTodoUserMaps) {
                    newAssignedUserIds.add(todoUserMap.getUser().getId());
                }
                newTodoDtos.get(i).setAssignedUserIds(newAssignedUserIds);
                i++;
            }
        }
        // random값 있을 때
        else if (todoDto.getRandomUsersNum() != null && !todoDto.getRandomUsersNum().equals(0)) {
            // assign user ramdomly
            // numRandomUsers & random -> todoId - userId mapping
            Integer randomUsersNum = todoDto.getRandomUsersNum();

            int i=0;
            for(Todo newTodo : newTodos) {
                List<User> users = userRepository.findByRoomId(user.getRoom().getId());
                List<TodoUserMap> todoUserMaps = new ArrayList<>();
                for (int j=1;j<=randomUsersNum;j++) {
                    Random random = new Random();
                    int randomIdx = random.nextInt(users.size());
                    User randomUser = users.get(randomIdx);
                    // 한 번 선택한 user는 다시 선택 불가능하게 users에서 제거
                    users.remove(randomIdx);

                    TodoUserMap todoUserMap = new TodoUserMap();
                    todoUserMap.setTodo(newTodo);
                    todoUserMap.setUser(randomUser);
                    todoUserMaps.add(todoUserMap);
                }
                List<TodoUserMap> newTodoUserMaps = todoUserMapRepository.saveAll(todoUserMaps);

                Set<Integer> newAssignedUserIds = new HashSet<>();

                for(TodoUserMap todoUserMap : newTodoUserMaps) {
                    newAssignedUserIds.add(todoUserMap.getUser().getId());
                }
                newTodoDtos.get(i).setAssignedUserIds(newAssignedUserIds);
                newTodoDtos.get(i).setRandomUsersNum(randomUsersNum);
                i++;
            }
        }

        return newTodoDtos;
    }

    @Override
    public void deleteTodo(Integer id) {

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        todoRepository.delete(todo);
    }

    @Override
    public void deleteTodos(Integer id) {

        // retrieve todo entity by id
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo","id",id));

        if(todo.getRepeatType().equals(RepeatType.NONE)) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "The todo is not repeating");
        }
        Integer repeatGroupId = todo.getId();
        if (todo.getRepeatGroupId() != null) repeatGroupId = todo.getRepeatGroupId();
        List<Todo> oldTodos = todoRepository.findByRepeatGroupId(repeatGroupId);
        for(Todo t : oldTodos) {
            if(t.getStartTime().isAfter(todo.getStartTime()))
                todoRepository.delete(t);
        }
        todoRepository.delete(todo);
    }

}
