package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt;

    private HttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;

    @BeforeEach
    void setUp() {
        authEntryPointJwt = new AuthEntryPointJwt();
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void testCommenceMethod() throws Exception {
        when(httpServletRequest.getServletPath()).thenReturn("/api/test");

        AuthenticationException authenticationException = new AuthenticationException("Unauthorized") {};

        authEntryPointJwt.commence(httpServletRequest, httpServletResponse, authenticationException);

        assertEquals(401, httpServletResponse.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, httpServletResponse.getContentType());
        assertTrue(httpServletResponse.getContentAsString().contains("Unauthorized"));
    }
}
