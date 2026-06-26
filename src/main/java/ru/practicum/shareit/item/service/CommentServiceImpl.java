package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentMapper mapper;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;

    @Override
    public CommentDto addComment(CommentCreateDto dto, Long itemId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!bookingStorage.existsByBookerIdAndItem_IdAndStatusAndEndLessThanEqual(
                userId, itemId, BookingStatus.APPROVED, now
        )) {
            throw new ValidationException("User cannot comment on this");
        }
        dto.setCreatedDate(now);
        Comment comment = mapper.toComment(dto, author, item);
        return mapper.toCommentDto(commentStorage.save(comment));
    }

}
