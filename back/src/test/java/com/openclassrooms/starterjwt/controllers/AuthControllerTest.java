package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testAuthenticateUser() {
        // Simulate a login process with valid credentials
        Long id = 1L;
        String email = "camille.legrand@mail.fr";
        String password = "1234";
        String firstName = "Camille";
        String lastName = "Legrand";
        boolean isAdmin = false;

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .id(id)
                .password(password)
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(
                User.builder().id(id).email(email).password(password).firstName(firstName).lastName(lastName).admin(isAdmin).build()
        ));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        JwtResponse responseBody = (JwtResponse) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, responseBody.getUsername());
        assertEquals(firstName, responseBody.getFirstName());
        assertEquals(lastName, responseBody.getLastName());
        assertEquals(id, responseBody.getId());
        assertEquals(isAdmin, responseBody.getAdmin());
        assertEquals("Bearer", responseBody.getType());
        assertNotNull(responseBody.getToken());
    }

    @Test
    public void testRegisterUser() {
        // Simulate a registration with a new email
        String email = "amelie.dupont@mail.fr";
        String password = "securePass";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName("Amélie");
        signupRequest.setLastName("Dupont");
        signupRequest.setPassword(password);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterUserEmailAlreadyTaken() {
        // Simulate registration failure due to existing email
        String email = "florent.lenoir@mail.fr";
        String password = "pass123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName("Florent");
        signupRequest.setLastName("Lenoir");
        signupRequest.setPassword(password);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        MessageResponse messageResponse = (MessageResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}
