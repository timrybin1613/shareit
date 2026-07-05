package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public Comment toComment(CommentCreateDto dto, User author, Item item) {
        return Comment.builder()
                .id(dto.getId())
                .item(item)
                .text(dto.getText())
                .author(author)
                .created(dto.getCreatedDate())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> toCommentDtos(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        return comments.stream().map(this::toCommentDto)
                .collect(Collectors.toCollection(ArrayList::new));

    }

}
