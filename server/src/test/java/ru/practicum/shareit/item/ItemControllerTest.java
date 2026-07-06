package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Test
    void getItems_shouldReturnItems() throws Exception {

        ItemDtoWithDetails item = ItemDtoWithDetails.builder()
                .id(1L)
                .name("Item")
                .description("description")
                .lastBooking(LocalDateTime.now().minusDays(1))
                .nextBooking(LocalDateTime.now().plusDays(1))
                .available(true)
                .build();

        when(itemService.findAllForUser(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()));

        verify(itemService).findAllForUser(1L);
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {

        ItemDtoWithDetails item = ItemDtoWithDetails.builder()
                .id(1L)
                .name("Item")
                .description("description")
                .lastBooking(LocalDateTime.now().minusDays(1))
                .nextBooking(LocalDateTime.now().plusDays(1))
                .available(true)
                .build();

        when(itemService.findById(1L, 1L)).thenReturn(item);

        mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService).findById(1L, 1L);
    }

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {

        ItemCreateDto dto = ItemCreateDto.builder()
                .available(true)
                .name("item")
                .description("description")
                .build();

        ItemDto item = ItemDto.builder()
                .id(1L)
                .available(true)
                .name("item")
                .description("description")
                .build();

        when(itemService.create(any(ItemCreateDto.class), eq(1L))).thenReturn(item);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("description"));

        verify(itemService)
                .create(any(ItemCreateDto.class), eq(1L));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {

        ItemUpdateDto dto = new ItemUpdateDto();
        dto.setName("item");
        dto.setDescription("description");
        dto.setAvailable(true);

        ItemDto item = ItemDto.builder()
                .id(1L)
                .available(true)
                .name("item")
                .description("description")
                .build();

        when(itemService.update(eq(1L), any(ItemUpdateDto.class), eq(1L))).thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("description"));

        verify(itemService)
                .update(eq(1L), any(ItemUpdateDto.class), eq(1L));

    }

    @Test
    void searchItem_shouldReturnItems() throws Exception {

        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .build();

        when(itemService.findAvailableItemsByText("item"))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("item"))
                .andExpect(jsonPath("$[0].description").value("item description"))
                .andExpect(jsonPath("$[0].available").value(true));

        verify(itemService).findAvailableItemsByText("item");
    }

    @Test
    void addComment_shouldReturnCreatedComment() throws Exception {

        CommentCreateDto createDto = CommentCreateDto.builder()
                .text("item comment")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("item comment")
                .authorName("Ivan")
                .created(LocalDateTime.now())
                .build();

        when(commentService.addComment(any(CommentCreateDto.class), eq(1L), eq(1L)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("item comment"))
                .andExpect(jsonPath("$.authorName").value("Ivan"));

        verify(commentService)
                .addComment(any(CommentCreateDto.class), eq(1L), eq(1L));
    }

}
