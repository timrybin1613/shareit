package ru.practicum.shareit.exception;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class ErrorResponse {
    private Timestamp timestamp;
    private int status;
    private List<String> error;

    public ErrorResponse(int status, List<String> error) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.error = error;
    }
}
