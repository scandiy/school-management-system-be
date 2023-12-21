package com.students2.student2.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class", schema = "school")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "classes", fetch = FetchType.LAZY)
    private Set<Subject> subjects = new HashSet<>();

    public Class(Long id, String name, Set<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }

    public Class() {
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

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.getClasses().add(this);
    }

    public void deleteSubject(Subject subject) {
        subjects.remove(subject);
        subject.getClasses().remove(this);
    }
}
