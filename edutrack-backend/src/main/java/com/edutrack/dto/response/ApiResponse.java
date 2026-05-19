package com.edutrack.dto.response;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // ── Constructors ────────────────────────────────────
    public ApiResponse() {}

    public ApiResponse(boolean success,
                       String message,
                       T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // ── Static helper methods ───────────────────────────

    // success with data only
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }

    // success with message and data
    public static <T> ApiResponse<T> success(
            String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    // error with message
    public static <T> ApiResponse<T> error(
            String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    // ── Getters ─────────────────────────────────────────
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    // ── Setters ─────────────────────────────────────────
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}