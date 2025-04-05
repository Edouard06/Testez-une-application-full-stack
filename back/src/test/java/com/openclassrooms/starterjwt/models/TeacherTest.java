package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher;
    private final String lastName = "Smith";
    private final String firstName = "Alice";

    /**
     * Initialize a Teacher instance before each test.
     */
    @BeforeEach
    public void setUp() {
        teacher = Teacher.builder()
                .id(10L)
                .lastName("Brown")
                .firstName("Emma")
                .build();
    }

    /**
     * Test that the ID can be set and retrieved correctly.
     */
    @Test
    public void shouldSetAndGetTeacherId() {
        teacher.setId(99L);
        assertEquals(99L, teacher.getId());
    }

    /**
     * Test that the last name can be set and retrieved correctly.
     */
    @Test
    public void shouldSetAndGetLastName() {
        teacher.setLastName(lastName);
        assertEquals(lastName, teacher.getLastName());
    }

    /**
     * Test that the first name can be set and retrieved correctly.
     */
    @Test
    public void shouldSetAndGetFirstName() {
        teacher.setFirstName(firstName);
        assertEquals(firstName, teacher.getFirstName());
    }

    /**
     * Test the toString() method returns the expected string.
     */
    @Test
    public void shouldReturnCorrectToString() {
        String expected = "Teacher(id=10, lastName=Brown, firstName=Emma, createdAt=null, updatedAt=null)";
        assertEquals(expected, teacher.toString());
    }

    /**
     * Test that two teachers with the same properties are considered equal.
     */
    @Test
    public void shouldBeEqualWhenSameProperties() {
        Teacher teacher1 = Teacher.builder().id(5L).lastName("Clark").firstName("John").build();
        Teacher teacher2 = Teacher.builder().id(5L).lastName("Clark").firstName("John").build();
        assertEquals(teacher1, teacher2);
    }
}
