package com.students2.student2.dtos;

import com.students2.student2.entities.Class;
import java.util.Set;

public class SubjectDTO {
    private Long id;
    private String name;
    private Set<Class> classes;

    public SubjectDTO() {
    }

    public SubjectDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }
}
