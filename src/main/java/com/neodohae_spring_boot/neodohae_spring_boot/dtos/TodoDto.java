package com.neodohae_spring_boot.neodohae_spring_boot.dtos;

import lombok.Data;

@Data
public class TodoDto {
    private Long id;
    private String title;
    private String description;
}
