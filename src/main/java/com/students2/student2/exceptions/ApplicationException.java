package com.students2.student2.exceptions;

public class ApplicationException extends  RuntimeException{
    private final String code;

    private final String message;

    private final HttpStatusCode httpCode;

    public ApplicationException(String code, String message, HttpStatusCode httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatusCode getHttpCode() {
        return httpCode;
    }
}
