package com.students2.student2.dtos;

public class StudentDTO {

    private Long id;
    private String name;
    private Long classId;

    public StudentDTO() {

    }

    public StudentDTO(Long id, String name, Long classId) {
        this.id = id;
        this.name = name;
        this.classId = classId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
