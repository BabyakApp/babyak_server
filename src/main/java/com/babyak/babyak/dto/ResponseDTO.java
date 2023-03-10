package com.babyak.babyak.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@RequiredArgsConstructor(staticName = "of")
public class ResponseDTO<T> {

    private final int statusCode;
    private final String message;
    private final T data;

}
