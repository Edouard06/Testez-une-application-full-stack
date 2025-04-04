package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionMapperImplTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper = new SessionMapperImpl();

    @Test
    void toEntity_WithValidDto_ReturnsCorrectEntity() {
        // Should convert a SessionDto to a Session entity
        SessionDto sessionDto = createSessionDto(1L, "Atelier Printemps");

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // Should convert a list of SessionDto to a list of Session entities
        SessionDto sessionDto1 = createSessionDto(1L, "Cours de Yoga");
        SessionDto sessionDto2 = createSessionDto(2L, "Pilates pour Tous");
        List<SessionDto> dtoList = new ArrayList<>();
        dtoList.add(sessionDto1);
        dtoList.add(sessionDto2);

        List<Session> entityList = sessionMapper.toEntity(dtoList);

        assertEquals(dtoList.size(), entityList.size());
        assertEquals(sessionDto1.getId(), entityList.get(0).getId());
        assertEquals(sessionDto1.getName(), entityList.get(0).getName());
    }

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // Should convert a list of Session entities to a list of SessionDto
        Session session1 = createSession(1L, "Sophrologie");
        Session session2 = createSession(2L, "Relaxation Zen");
        List<Session> entityList = Arrays.asList(session1, session2);

        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        assertEquals(entityList.size(), dtoList.size());
        assertEquals(session1.getId(), dtoList.get(0).getId());
        assertEquals(session1.getName(), dtoList.get(0).getName());
    }

    @Test
    void testToDto_WithValidSession_ReturnsCorrectDto() {
        // Should correctly map a Session entity with a teacher to SessionDto
        Teacher teacherWithId = Teacher.builder().id(99L).firstName("Claire").lastName("Moreau").build();
        Session session = Session.builder()
                .id(1L)
                .name("Stretching Matinal")
                .description("30 minutes de réveil musculaire")
                .teacher(teacherWithId)
                .build();

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(99L, sessionDto.getTeacher_id());
    }

    @Test
    void testToDto_WithSessionWithoutTeacher_ReturnsDtoWithNullTeacherId() {
        // Should handle case where session has no teacher set
        Session sessionWithoutTeacher = Session.builder()
                .id(2L)
                .name("Méditation Guidée")
                .description("Séance de méditation de 15 minutes")
                .build();

        SessionDto sessionDto = sessionMapper.toDto(sessionWithoutTeacher);

        assertEquals(sessionWithoutTeacher.getId(), sessionDto.getId());
        assertEquals(sessionWithoutTeacher.getName(), sessionDto.getName());
        assertEquals(sessionWithoutTeacher.getDescription(), sessionDto.getDescription());
        assertNull(sessionDto.getTeacher_id());
    }

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // Should return null when Session entity is null
        SessionDto result = sessionMapper.toDto((Session) null);
        assertNull(result);
    }

    @Test
    void toDtoList_WithNullEntityList_ReturnsNull() {
        // Should return null when entity list is null
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);
        assertNull(result);
    }

    @Test
    void toEntityList_WithNullListDto_ReturnsNull() {
        // Should return null when DTO list is null
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);
        assertNull(result);
    }

    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        // Should return null when DTO is null
        Session result = sessionMapper.toEntity((SessionDto) null);
        assertNull(result);
    }

    // Helpers

    private SessionDto createSessionDto(Long id, String name) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);
        sessionDto.setDescription("Description for " + name);
        return sessionDto;
    }

    private Session createSession(Long id, String name) {
        return Session.builder()
                .id(id)
                .name(name)
                .description("Activity: " + name)
                .build();
    }
}
