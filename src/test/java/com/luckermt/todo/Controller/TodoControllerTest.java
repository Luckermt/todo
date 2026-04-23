package com.luckermt.todo.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luckermt.todo.Model.Priority;
import com.luckermt.todo.Model.Todo;
import com.luckermt.todo.Service.TodoService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService todoService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getAllTodos_shouldReturnList() throws Exception {
		Todo todo = new Todo("Test", "Desc", false, Priority.HIGH, LocalDateTime.of(2026, 4, 22, 10, 0));
		todo.setId(1L);
		when(todoService.getAllTodos()).thenReturn(List.of(todo));

		mockMvc.perform(get("/api/todos"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].title").value("Test"));
	}

}