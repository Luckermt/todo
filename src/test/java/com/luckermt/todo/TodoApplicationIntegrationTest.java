package com.luckermt.todo;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.luckermt.todo.Model.Priority;
import com.luckermt.todo.Model.Todo;
import com.luckermt.todo.Repository.TodoRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class TodoApplicationIntegrationTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		registry.add("spring.sql.init.mode", () -> "never");
	}

	@BeforeEach
	void setUp() {
		todoRepository.deleteAll();
	}

	@Test
	void fullCrudFlow_shouldWorkCorrectly() throws Exception {
		// Создание задачи
		Todo newTodo = new Todo("Integration Test", "Testing all layers", false, Priority.MEDIUM,
				LocalDateTime.of(2026, 5, 1, 9, 0));

		String responseString = mockMvc
			.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newTodo)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andReturn()
			.getResponse()
			.getContentAsString();

		Todo created = objectMapper.readValue(responseString, Todo.class);
		Long id = created.getId();

		// Получение по ID
		mockMvc.perform(get("/api/todos/" + id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Integration Test"));

		// Обновление задачи
		created.setCompleted(true);
		created.setPriority(Priority.HIGH);
		mockMvc
			.perform(put("/api/todos/" + id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(created)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.completed").value(true));

		// Получение всех задач (проверка сортировки)
		Todo highPriorityTodo = new Todo("High Priority", "Do first", false, Priority.HIGH,
				LocalDateTime.now().plusDays(2));
		mockMvc
			.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(highPriorityTodo)))
			.andExpect(status().isCreated());

		mockMvc.perform(get("/api/todos"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].priority").value("HIGH")); // HIGH приоритеты первые

		// Удаление задачи
		mockMvc.perform(delete("/api/todos/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get("/api/todos/" + id)).andExpect(status().isNotFound());
	}

}