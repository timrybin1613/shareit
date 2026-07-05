package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentCreateDto {
    private Long id;
    @NotBlank
    private String text;
    @NotBlank
    private Long itemId;
    @NotNull
    private Long authorId;
    private LocalDateTime createdDate;
}
