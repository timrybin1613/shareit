package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;

public interface CommentService {
    CommentDto addComment(CommentCreateDto dto, Long itemId, Long userId);
}
