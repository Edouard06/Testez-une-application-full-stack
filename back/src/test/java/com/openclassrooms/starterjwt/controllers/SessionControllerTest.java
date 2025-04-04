package com.openclassrooms.starterjwt.controllers;

import static org.mockito.ArgumentMatchers.any;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private List<Session> sessions;
    private List<SessionDto> sessionDtos;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionController = new SessionController(sessionService, sessionMapper);

        session = new Session();
        session.setId(1L);
        session.setDescription("Test session");

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test session");

        sessions = Collections.singletonList(session);
        sessionDtos = Collections.singletonList(sessionDto);
    }

    @Test
    public void findAllTest() {
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    public void findByIdTest() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void createTest() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void updateTest() {
        Session updatedSession = new Session();
        updatedSession.setId(1L);
        updatedSession.setDescription("Updated session");

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setId(1L);
        updatedSessionDto.setDescription("Updated session");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(updatedSession);
        when(sessionService.update(1L, updatedSession)).thenReturn(updatedSession);
        when(sessionMapper.toDto(updatedSession)).thenReturn(updatedSessionDto);

        ResponseEntity<?> response = sessionController.update("1", updatedSessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSessionDto, response.getBody());
    }

    @Test
    public void saveTest() {
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void participateTest() {
        ResponseEntity<?> response = sessionController.participate("1", "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void noLongerParticipateTest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testParticipateBadRequest() {
        ResponseEntity<?> response = sessionController.participate("", "");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipateBadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("", "");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindSessionById_NotFound() {
        when(sessionService.getById(99L)).thenReturn(null);
        ResponseEntity<?> response = sessionController.findById("99");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindSessionById_BadRequest() {
        ResponseEntity<?> response = sessionController.findById("invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateSession_BadRequest() {
        ResponseEntity<?> response = sessionController.update("invalid", null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteSession_NotFound() {
        when(sessionService.getById(999L)).thenReturn(null);
        ResponseEntity<?> response = sessionController.save("999");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteSession_BadRequest() {
        ResponseEntity<?> response = sessionController.save("notAnId");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("fake", "user");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
