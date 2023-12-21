package com.students2.student2.exceptions;

public class StudentMovingToAnotherClassException extends ApplicationException {

    public StudentMovingToAnotherClassException() {
        super(String.valueOf(ExceptionCodes.EXCEPTION_05), "Student can not be moved to another class. Average marks less then 3", HttpStatusCode.NOT_ACCEPTABLE);
    }
}