package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public Object create(@RequestBody ItemRequestCreateDto dto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.createRequest(userId, dto);
    }

    @GetMapping
    public Object getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public Object getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public Object getById(@PathVariable("requestId") Long requestId) {
        log.info("RequestId - {} ", requestId);
        return requestClient.getById(requestId);
    }
}
