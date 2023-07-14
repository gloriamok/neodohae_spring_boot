package com.neodohae_spring_boot.neodohae_spring_boot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class TodoDto {
    private Integer id;
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotEmpty
    private String status;
    private Integer repeatGroupId;
    private LocalDateTime repeatEndTime;
    @NotEmpty
    private String repeatType;

    private Set<Integer> assignedUserIds;
    private Integer randomUsersNum;
    @NotNull
    private Integer userId;
}
