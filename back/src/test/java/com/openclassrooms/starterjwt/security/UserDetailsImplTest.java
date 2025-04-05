package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        // Initialize UserDetailsImpl with all required properties
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("Michel")
                .lastName("Michel")
                .password("cProgrammation")
                .admin(true)
                .build();
    }

    @Test
    public void testGetAuthorities_ShouldReturnEmpty() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty(), "Authorities list should be empty by default");
    }

    @Test
    public void testIsAccountNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired(), "Account should be non-expired");
    }

    @Test
    public void testIsAccountNonLocked_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked(), "Account should be non-locked");
    }

    @Test
    public void testIsCredentialsNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired(), "Credentials should be non-expired");
    }

    @Test
    public void testIsEnabled_ShouldReturnTrue() {
        assertTrue(userDetails.isEnabled(), "Account should be enabled");
    }

    @Test
    public void testEquals_WithSameId_ShouldReturnTrue() {
        UserDetailsImpl anotherUser = UserDetailsImpl.builder().id(1L).build();
        assertEquals(userDetails, anotherUser, "Users with the same ID should be equal");
    }

    @Test
    public void testEquals_WithDifferentId_ShouldReturnFalse() {
        UserDetailsImpl anotherUser = UserDetailsImpl.builder().id(2L).build();
        assertNotEquals(userDetails, anotherUser, "Users with different IDs should not be equal");
    }
}
