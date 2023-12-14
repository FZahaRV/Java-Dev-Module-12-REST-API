package com.goit.generic_response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T> {
    private Error error;
    private T data;

    public enum Error {
        OK,
        INVALID_EMAIL,
        USER_ALREADY_EXISTS,
        INVALID_NAME,
        INVALID_AGE,
        INVALID_PASSWORD,
        INVALID_TITLE,
        INVALID_CONTENT
    }

    public static <T> Response<T> success(T data) {
        return Response.<T>builder().error(Error.OK).data(data).build();
    }

    public static <T> Response<T> failed(Error error) {
        return Response.<T>builder().error(error).build();
    }
}