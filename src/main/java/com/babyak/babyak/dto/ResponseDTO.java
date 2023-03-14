package com.babyak.babyak.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {

    private int statusCode;
    private String message;
    private T data;

    public ResponseDTO (int statusCode) {
        this.statusCode = statusCode;
        this.message = null;
        this.data = null;
    }

    public ResponseDTO (int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> ResponseDTO<T> response (int statusCode) {
        return response(statusCode, null, null);
    }

    public static<T> ResponseDTO<T> response(int statusCode, String message, T data) {
        return ResponseDTO.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

}
