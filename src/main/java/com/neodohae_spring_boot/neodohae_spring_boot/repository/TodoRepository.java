package com.neodohae_spring_boot.neodohae_spring_boot.repository;

import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
