package com.students2.student2.exceptions;

public class SubjectNotFoundException extends ApplicationException {

    public SubjectNotFoundException() {
        super(String.valueOf(ExceptionCodes.EXCEPTION_03), "Subject not found", HttpStatusCode.NOT_FOUND);
    }
}
