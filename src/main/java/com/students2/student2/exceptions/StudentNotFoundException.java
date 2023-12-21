package com.students2.student2.exceptions;

public class StudentNotFoundException extends ApplicationException {

    public StudentNotFoundException() {
        super(String.valueOf(ExceptionCodes.EXCEPTION_01), "Student not found", HttpStatusCode.NOT_FOUND);
    }
}
