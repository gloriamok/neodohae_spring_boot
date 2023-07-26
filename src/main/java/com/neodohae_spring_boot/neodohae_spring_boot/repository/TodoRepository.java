package com.neodohae_spring_boot.neodohae_spring_boot.repository;

import com.neodohae_spring_boot.neodohae_spring_boot.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByRepeatGroupId(Integer repeatGroupId);
    // List<Todo> findByUserIdAndStartTimeOrEndTimeBetween(Integer userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId AND ((t.startTime BETWEEN :startOfMonth AND :endOfMonth) OR (t.endTime BETWEEN :startOfMonth AND :endOfMonth))")
    List<Todo> findTodosForCurrentMonth(@Param("userId") Integer userId, @Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);
}
