package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TeacherMapperImplTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper = new TeacherMapperImpl();

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // Should convert a list of Teacher entities to a list of TeacherDto
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        List<Teacher> entityList = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        assertEquals(entityList.size(), dtoList.size());
        assertEquals(teacher1.getId(), dtoList.get(0).getId());
        assertEquals(teacher1.getLastName(), dtoList.get(0).getLastName());
        assertEquals(teacher1.getFirstName(), dtoList.get(0).getFirstName());
        assertEquals(teacher2.getId(), dtoList.get(1).getId());
        assertEquals(teacher2.getLastName(), dtoList.get(1).getLastName());
        assertEquals(teacher2.getFirstName(), dtoList.get(1).getFirstName());
    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // Should convert a list of TeacherDto to a list of Teacher entities
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Doe");
        teacherDto1.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setLastName("Smith");
        teacherDto2.setFirstName("Jane");

        List<TeacherDto> dtoList = Arrays.asList(teacherDto1, teacherDto2);
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        assertEquals(dtoList.size(), entityList.size());
        assertEquals(teacherDto1.getId(), entityList.get(0).getId());
        assertEquals(teacherDto1.getLastName(), entityList.get(0).getLastName());
        assertEquals(teacherDto1.getFirstName(), entityList.get(0).getFirstName());
        assertEquals(teacherDto2.getId(), entityList.get(1).getId());
        assertEquals(teacherDto2.getLastName(), entityList.get(1).getLastName());
        assertEquals(teacherDto2.getFirstName(), entityList.get(1).getFirstName());
    }

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // Should return null when mapping from null entity to DTO
        TeacherDto result = teacherMapper.toDto((Teacher) null);
        assertNull(result);
    }

    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        // Should return null when mapping from null DTO to entity
        Teacher result = teacherMapper.toEntity((TeacherDto) null);
        assertNull(result);
    }

    @Test
    void toDtoList_WithNullEntityList_ReturnsNull() {
        // Should return null when mapping from null entity list
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);
        assertNull(dtoList);
    }

    @Test
    void toEntityList_WithNullDtoList_ReturnsNull() {
        // Should return null when mapping from null DTO list
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);
        assertNull(entityList);
    }
}
