package com.students2.student2.exceptions;

public class MarkNotFoundException extends ApplicationException {

    public MarkNotFoundException() {
        super(String.valueOf(ExceptionCodes.EXCEPTION_02), "Mark not found", HttpStatusCode.NOT_FOUND);
    }
}
