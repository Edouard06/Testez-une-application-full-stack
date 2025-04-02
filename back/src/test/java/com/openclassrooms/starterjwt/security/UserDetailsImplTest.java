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
        // Initialize the UserDetailsImpl object before each test
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("Michel")
                .lastName("Michel")
                .admin(true)
                .password("cProgrammation")
                .build();
    }

    @Test
    public void testGetAuthorities() {
        // Execution: call the getAuthorities() method
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Verification: ensure the returned collection is not null
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());  // In this case, we expect the collection to be empty
    }

    @Test
    public void testIsAccountNonExpired() {
        // Execution: call the isAccountNonExpired() method
        boolean isAccountNonExpired = userDetails.isAccountNonExpired();

        // Verification: ensure the account is not expired
        assertTrue(isAccountNonExpired);
    }

    @Test
    public void testIsAccountNonLocked() {
        // Execution: call the isAccountNonLocked() method
        boolean isAccountNonLocked = userDetails.isAccountNonLocked();

        // Verification: ensure the account is not locked
        assertTrue(isAccountNonLocked);
    }

    @Test
    public void testIsCredentialsNonExpired() {
        // Execution: call the isCredentialsNonExpired() method
        boolean isCredentialsNonExpired = userDetails.isCredentialsNonExpired();

        // Verification: ensure the credentials are not expired
        assertTrue(isCredentialsNonExpired);
    }

    @Test
    public void testIsEnabled() {
        // Execution: call the isEnabled() method
        boolean isEnabled = userDetails.isEnabled();

        // Verification: ensure the account is enabled
        assertTrue(isEnabled);
    }

    @Test
    public void testEqualsSameObject() {
        // Prepare: create another UserDetailsImpl object with the same ID
        UserDetailsImpl sameUserDetails = UserDetailsImpl.builder()
                .id(1L)
                .build();

        // Verification: ensure both objects are equal
        assertEquals(userDetails, sameUserDetails);
    }

    @Test
    public void testEqualsDifferentObject() {
        // Prepare: create another UserDetailsImpl object with a different ID
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder()
                .id(2L)
                .build();

        // Verification: ensure both objects are not equal
        assertNotEquals(userDetails, differentUserDetails);
    }
}
