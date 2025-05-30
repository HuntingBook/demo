package com.flightapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A generic base class for API responses.
 * It standardizes the response structure across the application, indicating success or failure,
 * an HTTP-like status code, a message, and an optional data payload.
 *
 * @param <T> The type of the data payload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    /**
     * Indicates whether the API call was successful.
     */
    private boolean success;
    /**
     * An HTTP-like status code representing the outcome of the API call.
     */
    private int code;
    /**
     * A descriptive message about the outcome of the API call.
     */
    private String message;
    /**
     * The data payload of the response, generic type.
     */
    private T data;

    /**
     * Creates a success response with data and a custom message.
     *
     * @param data The data payload.
     * @param message The success message.
     * @param <T> The type of the data.
     * @return A {@link BaseResponse} indicating success.
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(true, 200, message, data);
    }

    /**
     * Creates a success response with data and a default "OK" message.
     *
     * @param data The data payload.
     * @param <T> The type of the data.
     * @return A {@link BaseResponse} indicating success.
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, 200, "OK", data);
    }

    /**
     * Creates an error response with a specific code and message, and no data payload.
     *
     * @param code The error code.
     * @param message The error message.
     * @param <T> The type of the data (will be null for this error response).
     * @return A {@link BaseResponse} indicating an error.
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(false, code, message, null);
    }

    /**
     * Creates an error response with a specific code, message, and an optional data payload.
     * This is useful for returning structured error information, like validation errors.
     *
     * @param code The error code.
     * @param message The error message.
     * @param data The optional data payload for the error.
     * @param <T> The type of the data.
     * @return A {@link BaseResponse} indicating an error.
     */
    public static <T> BaseResponse<T> error(int code, String message, T data) {
        return new BaseResponse<>(false, code, message, data);
    }
}
