package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.util.HeaderConstants;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public Object create(
            @Valid
            @RequestBody ItemRequestCreateDto dto,
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return requestClient.createRequest(userId, dto);
    }

    @GetMapping
    public Object getByUserId(@RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return requestClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public Object getAll(@RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return requestClient.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public Object getById(@PathVariable("requestId") Long requestId) {
        log.info("RequestId - {} ", requestId);
        return requestClient.getById(requestId);
    }
}
