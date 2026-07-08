package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ItemCreateDto {
    private Long id;
    private String name;
    private String description;
    private Long requestId;
    @NonNull
    private Boolean available;
}
