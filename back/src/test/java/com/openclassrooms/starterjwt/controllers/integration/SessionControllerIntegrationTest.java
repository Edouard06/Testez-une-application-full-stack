package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;

    /**
     * Generates a valid JWT token before each test to simulate an authenticated user
     */
    @BeforeEach
    void beforeEach() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    /**
     * Should return 200 OK when authenticated user fetches all sessions
     */
    @Test
    public void testSessionFindAll() throws Exception {
        mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    /**
     * Should return 401 Unauthorized when invalid JWT is used
     */
    @Test
    public void testSessionFindAllUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session").header("Authorization", "Bearer not_a_jwt"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Should return 404 Not Found if session ID does not exist
     */
    @Test
    public void testSessionFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/session/0").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    /**
     * Should return 400 Bad Request if session ID is not numeric
     */
    @Test
    public void testSessionFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/notAnId").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should return 200 OK after successful session creation
     */
    @Test
    public void testSessionCreate() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("test create session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(post("/api/session").header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());
    }

    /**
     * Should return 200 OK when updating an existing session
     */
    @Test
    public void testSessionUpdate() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("updated session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(put("/api/session/2").header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());
    }

    /**
     * Should return 400 Bad Request if session ID is invalid during update
     */
    @Test
    public void testSessionUpdateBadRequest() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("updated session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(put("/api/session/notAnId").header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should return 404 Not Found when deleting a non-existent session
     */
    @Test
    public void testSessionDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/session/0").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    /**
     * Should return 400 Bad Request if session ID is invalid during deletion
     */
    @Test
    public void testSessionDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/notgoodId").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should return 200 OK when user participates in a session
     */
    @Test
    public void testSessionParticipateOK() throws Exception {
        mockMvc.perform(post("/api/session/2/participate/1").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    /**
     * Should return 400 Bad Request if user ID is invalid when participating
     */
    @Test
    public void testSessionParticipateBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/2/participate/wrongId").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should return 400 Bad Request if user ID is invalid when cancelling participation
     */
    @Test
    public void testSessionNoLongerParticipateBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/2/participate/wrongId").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }
}
