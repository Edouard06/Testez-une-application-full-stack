package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherService;

    // Test the endpoint that returns all teachers
    @Test
    public void testFindAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder().id(1L).firstName("Claire").lastName("Lemoine").build());
        teachers.add(Teacher.builder().id(2L).firstName("Antoine").lastName("Dupuis").build());
        teachers.add(Teacher.builder().id(3L).firstName("Sophie").lastName("Marceau").build());

        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(new TeacherDto());
        teacherDtos.add(new TeacherDto());
        teacherDtos.add(new TeacherDto());

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        TeacherController controller = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = controller.findAll();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(teacherDtos, response.getBody());
    }

    // Test when a teacher is successfully found by ID
    @Test
    public void testFindTeacherById_Success() {
        Long id = 5L;
        Teacher teacher = Teacher.builder().id(id).firstName("Emma").lastName("Durand").build();

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);
        teacherDto.setFirstName("Emma");
        teacherDto.setLastName("Durand");

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        TeacherController controller = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = controller.findById(id.toString());

        verify(teacherService).findById(id);
        verify(teacherMapper).toDto(teacher);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
    }

    // Test when the teacher is not found (null return)
    @Test
    public void testFindTeacherById_NotFound() {
        Long id = 99L;

        when(teacherService.findById(id)).thenReturn(null);

        TeacherController controller = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = controller.findById(id.toString());

        verify(teacherService).findById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    // Test when the ID is invalid (not a number)
    @Test
    public void testFindTeacherById_BadRequest() {
        TeacherController controller = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = controller.findById("invalid_id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
