package com.students2.student2.exceptions;

public class SubjectNotRelatedToStudentException extends ApplicationException {

    public SubjectNotRelatedToStudentException() {
        super(String.valueOf(ExceptionCodes.EXCEPTION_04), "Student has no relation to this subject!", HttpStatusCode.NOT_FOUND);
    }
}