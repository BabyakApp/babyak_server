package com.babyak.babyak.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Response {

    @Getter @Builder
    private static class Body {
        private int state;
        private String result;
        private String message;
        private Object data;
        private Object error;
    }

    // Success(data, msg, statusCode)
    public ResponseEntity<?> success(Object data, String msg, HttpStatus httpStatus) {
        Body body = Body.builder()
                .state(httpStatus.value())
                .result("success")
                .message(msg)
                .data(data)
                .error(Collections.emptyList())
                .build();

        return ResponseEntity.ok(body);
    }

    // Success(msg)
    public ResponseEntity<?> success(String msg) {
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }

    // Success(data)
    public ResponseEntity<?> success(Object data) {
        return success(data, null, HttpStatus.OK);
    }

    // Success( )
    public ResponseEntity<?> success() {
        return success(Collections.emptyList(), null, HttpStatus.OK);
    }



    // Fail(data, msg, statusCode)
    public ResponseEntity<?> fail(Object data, String msg, HttpStatus httpStatus) {
        Body body = Body.builder()
                .state(httpStatus.value())
                .result("fail")
                .message(msg)
                .data(data)
                .error(Collections.emptyList())
                .build();

        return ResponseEntity.ok(body);
    }

    // Fail(msg)
    public ResponseEntity<?> fail(String msg, HttpStatus httpStatus) {
        return success(Collections.emptyList(), msg, httpStatus);
    }

}
