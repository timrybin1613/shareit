package ru.practicum.shareit.item.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Nullable
    private Long requestId;
    @NonNull
    private Boolean available;
}
