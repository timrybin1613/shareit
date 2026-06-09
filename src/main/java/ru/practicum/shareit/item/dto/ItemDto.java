package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ItemDto {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NonNull
    private Boolean available;

}
