package com.students2.student2;

import com.students2.student2.dtos.ClassDTO;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.exceptions.StudentNotFoundException;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.services.schoolclass.ClassServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassServiceImpl classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllClasses() {
        List<SchoolClass> classes = List.of(
                new SchoolClass(1L, "10M"),
                new SchoolClass(2L, "11M")
        );
        when(classRepository.findAll()).thenReturn(classes);

        List<ClassDTO> result = classService.getAllClasses();

        assertEquals(2, result.size());
        assertEquals("10M", result.get(0).getName());
        assertEquals("11M", result.get(1).getName());
        verify(classRepository).findAll();
    }

    @Test
    public void testGetClassById_Exists() {
        Long classId = 1L;
        SchoolClass schoolClass = new SchoolClass(classId, "10M");
        when(classRepository.findById(classId)).thenReturn(Optional.of(schoolClass));

        ClassDTO result = classService.getClassById(classId);

        assertNotNull(result);
        assertEquals(classId, result.getId());
        assertEquals("10M", result.getName());
        verify(classRepository).findById(classId);
    }

    @Test
    public void testGetClassById_NotExists() {
        Long classId = 1L;
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        ClassDTO result = classService.getClassById(classId);

        assertNull(result);
        verify(classRepository).findById(classId);
    }

    @Test
    public void testCreateClass() {
        ClassDTO classDTO = new ClassDTO(null, "10M");
        SchoolClass savedClass = new SchoolClass(1L, "10M");
        when(classRepository.save(any(SchoolClass.class))).thenReturn(savedClass);

        ClassDTO result = classService.createClass(classDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("10M", result.getName());
        verify(classRepository).save(any(SchoolClass.class));
    }

    @Test
    public void testUpdateClass_Exists() {
        Long classId = 1L;
        ClassDTO updatedClassDTO = new ClassDTO(classId, "11M");
        SchoolClass schoolClass = new SchoolClass(classId, "10M");
        when(classRepository.findById(classId)).thenReturn(Optional.of(schoolClass));
        when(classRepository.save(any(SchoolClass.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClassDTO result = classService.updateClass(classId, updatedClassDTO);

        assertNotNull(result);
        assertEquals(classId, result.getId());
        assertEquals("11M", result.getName());
        verify(classRepository).findById(classId);
        verify(classRepository).save(any(SchoolClass.class));
    }

    @Test
    public void testUpdateClass_NotExists() {
        Long classId = 1L;
        ClassDTO updatedClassDTO = new ClassDTO(classId, "11M");
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        ClassDTO result = classService.updateClass(classId, updatedClassDTO);

        assertNull(result);
        verify(classRepository).findById(classId);
        verify(classRepository, never()).save(any(SchoolClass.class));
    }

    @Test
    public void testDeleteClass_Exists() {
        Long classId = 1L;
        when(classRepository.existsById(classId)).thenReturn(true);

        boolean result = classService.deleteClass(classId);

        assertTrue(result);
        verify(classRepository).existsById(classId);
        verify(classRepository).deleteById(classId);
    }

    @Test
    public void testDeleteClass_NotExists() {
        Long classId = 1L;
        when(classRepository.existsById(classId)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () ->
                classService.deleteClass(classId)
        );

        verify(classRepository).existsById(classId);
        verify(classRepository, never()).deleteById(classId);
    }
}
