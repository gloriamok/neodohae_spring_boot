package com.neodohae_spring_boot.neodohae_spring_boot.repository;

import com.neodohae_spring_boot.neodohae_spring_boot.model.TodoUserMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoUserMapRepository extends JpaRepository<TodoUserMap, Integer> {
    List<TodoUserMap> findByTodoId(Integer todoId);

    List<TodoUserMap> findByUserId(Integer userId);
}
