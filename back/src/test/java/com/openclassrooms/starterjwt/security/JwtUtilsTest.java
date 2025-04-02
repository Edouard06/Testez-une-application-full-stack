package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret = "testSecret";

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs = 60000; // 1 minute expiration for testing

    @BeforeEach
    public void setup() {
        jwtUtils = new JwtUtils();

        // Inject jwtSecret and jwtExpirationMs into the utility using reflection
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    public void shouldGenerateJwtTokenSuccessfully() {
        Authentication authMock = mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("testUser")
                .build();

        when(authMock.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authMock);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void shouldExtractUsernameFromToken() {
        String expectedUsername = "testUser";
        String token = Jwts.builder()
                .setSubject(expectedUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String actualUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void shouldValidateCorrectToken() {
        String token = Jwts.builder()
                .setSubject("validUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void shouldInvalidateExpiredToken() throws InterruptedException {
        String token = Jwts.builder()
                .setSubject("expiringUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000)) // 1 second expiration
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Thread.sleep(2000); // Wait to ensure expiration

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void shouldInvalidateTokenWithInvalidSignature() {
        String token = Jwts.builder()
                .setSubject("hackerUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void shouldInvalidateMalformedToken() {
        String malformedToken = "thisIsNotAValidToken";

        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    public void shouldInvalidateUnsupportedToken() {
        String unsupportedToken = Jwts.builder()
                .setPayload("UnsupportedPayload")
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    public void shouldValidateTokenWithEmptySubject() {
        String emptySubjectToken = Jwts.builder()
                .setSubject("") // Subject is empty but still a valid structure
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(emptySubjectToken));
    }
}
