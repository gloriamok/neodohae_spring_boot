package com.neodohae_spring_boot.neodohae_spring_boot.repository;

import com.neodohae_spring_boot.neodohae_spring_boot.model.TodoUserMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TodoUserMapRepository extends JpaRepository<TodoUserMap, Integer> {
    Set<TodoUserMap> findByTodoId(Integer todoId);
}
