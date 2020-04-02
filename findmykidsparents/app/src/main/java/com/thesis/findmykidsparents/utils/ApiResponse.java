package com.thesis.findmykidsparents.utils;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.thesis.findmykidsparents.utils.Status.ERROR;
import static com.thesis.findmykidsparents.utils.Status.LOADING;
import static com.thesis.findmykidsparents.utils.Status.SUCCESS;

public class ApiResponse<T> {

    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static <T> ApiResponse success(T data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }
}
