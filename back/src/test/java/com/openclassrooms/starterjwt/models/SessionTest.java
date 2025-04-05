package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

	private Session session;
	private Long id = 11L;
	private String name = "Yoga";
	private Date date = new Date();
	private String description = "Cours de Yoga";
	private Teacher teacher = new Teacher();
	private List<User> users = new ArrayList<>();
	private User user = new User();
	private LocalDateTime createdAt = LocalDateTime.MAX;
	private LocalDateTime updatedAt = LocalDateTime.MIN;

	@BeforeEach
	public void beforeEach() {
		this.session = Session.builder().id(id).name(name).date(date).description(description).teacher(teacher)
				.users(users).createdAt(createdAt).updatedAt(updatedAt).build();
	}

	@Test
	public void testSetID() {
		session.setId(110L);

		assertEquals(110L, session.getId());
	}

	@Test
	public void testSetName() {
		session.setName("YogaExpert");

		assertEquals("YogaExpert", session.getName());
	}

	@Test
	public void testSetDate() {
		Date dateTest = new Date();
		session.setDate(dateTest);

		assertEquals(session.getDate().hashCode(), dateTest.hashCode());
	}

	@Test
	public void testSetDescription() {
		session.setDescription("description Maj");

		assertEquals("description Maj", session.getDescription());
	}

	@Test
	public void testSetTeacher() {
		Teacher teacherTest = new Teacher();
		session.setTeacher(teacherTest);

		assertEquals(teacher.hashCode(), session.getTeacher().hashCode());
	}

	@Test
	public void testSetUsers() {
		ArrayList<User> userList = new ArrayList<>();
		userList.add(user);
		session.setUsers(userList);

		assertEquals(session.getUsers().get(0), user);
	}

	@Test
	public void testSetCreatedAt() {
		LocalDateTime nowDateStored = LocalDateTime.now();
		session.setCreatedAt(nowDateStored);

		assertEquals(session.getCreatedAt(), nowDateStored);
	}

	@Test
	public void testSetUpdatedAt() {
		LocalDateTime nowDateStored = LocalDateTime.now();
		session.setUpdatedAt(nowDateStored);

		assertEquals(session.getUpdatedAt(), nowDateStored);
	}

}