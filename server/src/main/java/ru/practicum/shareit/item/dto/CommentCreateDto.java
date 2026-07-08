package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentCreateDto {
    private Long id;
    private String text;
    private Long itemId;
    private Long authorId;
    private LocalDateTime createdDate;
}
