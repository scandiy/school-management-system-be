package com.students2.student2.dtos;

import java.util.Date;

public class MarkDTO {
    private Long id;
    private int value;
    private Long subjectId;
    private Long studentId;
    private Date date;

    public MarkDTO() {
    }

    public MarkDTO(Long id, int value, Long subjectId, Long studentId, Date date) {
        this.id = id;
        this.value = value;
        this.subjectId = subjectId;
        this.studentId = studentId;
        this.date = date;
    }

    public MarkDTO(int value, Long subjectId, Date date) {
        this.value = value;
        this.subjectId = subjectId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
