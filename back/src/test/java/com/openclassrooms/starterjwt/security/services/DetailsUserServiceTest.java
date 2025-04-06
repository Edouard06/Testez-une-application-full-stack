package com.openclassrooms.starterjwt.security.services; // <= même package que la classe testée

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DetailsUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername_ShouldReturnUserDetails() {
        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .firstName("Jacques")
                .lastName("Henry")
                .build();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        UserDetails userDetails = service.loadUserByUsername("test@mail.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_ShouldThrowException() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown@mail.com"));
    }
}
