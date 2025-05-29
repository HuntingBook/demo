package com.flightbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(true, 200, message, data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, 200, "OK", data);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(false, code, message, null);
    }
}
