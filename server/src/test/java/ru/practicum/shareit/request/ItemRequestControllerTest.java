package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemRequestService itemRequestService;

    @Test
    void create_shouldReturnCreatedRequest() throws Exception {

        ItemRequestCreateDto createDto = ItemRequestCreateDto.builder()
                .description("request")
                .build();

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("request")
                .build();

        when(itemRequestService.save(any(ItemRequestCreateDto.class), eq(1L)))
                .thenReturn(dto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("request"));

        verify(itemRequestService)
                .save(any(ItemRequestCreateDto.class), eq(1L));
    }

    @Test
    void getByUserId_shouldReturnRequests() throws Exception {

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("request")
                .build();

        when(itemRequestService.getByUserId(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("request"));

        verify(itemRequestService).getByUserId(1L);
    }

    @Test
    void getAll_shouldReturnRequests() throws Exception {

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("request")
                .build();

        when(itemRequestService.getAllForUser(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("request"));

        verify(itemRequestService).getAllForUser(1L);
    }

    @Test
    void getById_shouldReturnRequest() throws Exception {

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("request")
                .build();

        when(itemRequestService.getById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("request"));

        verify(itemRequestService).getById(1L);
    }

}
