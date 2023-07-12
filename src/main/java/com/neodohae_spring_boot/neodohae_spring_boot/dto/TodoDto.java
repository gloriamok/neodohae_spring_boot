package com.neodohae_spring_boot.neodohae_spring_boot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoDto {
    private Integer id;
    // title should not be null or empty
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 2, message = "Todo title should have at least 2 characters")
    private String title;
    // description should not be null or empty
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
    private String status;
    private Integer repeatGroupId;
    @NotNull
    private LocalDateTime repeatEndDateTime = LocalDateTime.parse("2030-01-01T12:00:00");
    private String repeatType;
}
