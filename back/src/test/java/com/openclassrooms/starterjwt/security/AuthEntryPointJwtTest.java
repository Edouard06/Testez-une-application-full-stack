package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Test
    public void testCommenceMethod() throws ServletException, IOException {
        // Simulate the servlet path of the request
        when(httpServletRequest.getServletPath()).thenReturn("path/to/servlet");

        // Create a simulated authentication exception
        AuthenticationException authenticationException = new AuthenticationException("ExceptionMessage") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };

        // Call the commence method of AuthEntryPointJwt
        authEntryPointJwt.commence(httpServletRequest, httpServletResponse, authenticationException);

        // Check that the response has status 401 and JSON content type
        assertEquals(httpServletResponse.getStatus(), HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(httpServletResponse.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }
}
