package com.easyplexdemoapp.data.remote;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ErrorHandling<T> {


    @NonNull public final Status status;
    @Nullable public final T data;
    @Nullable public final String message;

    private ErrorHandling(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ErrorHandling<T> success(@NonNull T data) {
        return new ErrorHandling<>(Status.SUCCESS, data, null);
    }

    public static <T> ErrorHandling<T> error(String msg, @Nullable T data) {
        return new ErrorHandling<>(Status.ERROR, data, msg);
    }

    public static <T> ErrorHandling<T> erroRegister(@NonNull T data) {
        return new ErrorHandling<>(Status.ERROR, data, null);
    }

    public static <T> ErrorHandling<T> loading(@Nullable T data) {
        return new ErrorHandling<>(Status.LOADING, data, null);
    }


    public enum Status { SUCCESS, ERROR, LOADING }
}