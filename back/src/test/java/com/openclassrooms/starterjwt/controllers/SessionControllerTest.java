package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    // READ - Find all sessions
    @Test
    public void testFindAllSessions() {
        List<Session> sessions = List.of(new Session(), new Session(), new Session());
        List<SessionDto> sessionDtos = List.of(new SessionDto(), new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    public void testFindSessionById_Success() {
        Long id = 2L;
        Session session = Session.builder().id(id).name("Atelier Peinture").build();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName("Atelier Peinture");

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.findById(id.toString());

        verify(sessionService).getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testFindSessionById_NotFound() {
        when(sessionService.getById(99L)).thenReturn(null);
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.findById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindSessionById_BadRequest() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // CREATE
    @Test
    public void testCreateSession() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Séance Méditation");

        Session session = Session.builder().name("Séance Méditation").build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.create(sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    // UPDATE
    @Test
    public void testUpdateSession_Success() {
        Long id = 3L;
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName("Séance Yoga");

        Session session = Session.builder().id(id).name("Séance Yoga").build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.update(id.toString(), sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void testUpdateSession_BadRequest() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.update("invalid", null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // DELETE
    @Test
    public void testDeleteSession_Success() {
        Long id = 4L;
        Session session = Session.builder().id(id).name("Séance Pilates").build();

        when(sessionService.getById(id)).thenReturn(session);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.save(id.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteSession_NotFound() {
        Long id = 999L;
        when(sessionService.getById(id)).thenReturn(null);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.save(id.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteSession_BadRequest() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.save("notAnId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // PARTICIPATE
    @Test
    public void testParticipate_BadRequest() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.participate("notValid", "wrongId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_Success() {
        Long userId = 5L;
        Long sessionId = 6L;

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.noLongerParticipate(sessionId.toString(), userId.toString());

        verify(sessionService).noLongerParticipate(sessionId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_BadRequest() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.noLongerParticipate("fake", "user");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
