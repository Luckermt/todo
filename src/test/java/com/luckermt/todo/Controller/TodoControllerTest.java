package com.luckermt.todo.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

	@Test
	void getTodoById_whenExists_shouldReturnTodo() throws Exception {
		Todo todo = new Todo("Test", "Desc", false, Priority.MEDIUM, null);
		todo.setId(1L);
		when(todoService.getTodoById(1L)).thenReturn(Optional.of(todo));

		mockMvc.perform(get("/api/todos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void getTodoById_whenNotExists_shouldReturn404() throws Exception {
		when(todoService.getTodoById(99L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/todos/99")).andExpect(status().isNotFound());
	}

	@Test
	void createTodo_shouldReturnCreated() throws Exception {
		Todo newTodo = new Todo("New", "New desc", false, Priority.LOW, LocalDateTime.now());
		Todo saved = new Todo("New", "New desc", false, Priority.LOW, null);
		saved.setId(1L);
		when(todoService.createTodo(any(Todo.class))).thenReturn(saved);

		mockMvc
			.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newTodo)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void updateTodo_whenExists_shouldReturnUpdated() throws Exception {
		Todo updateData = new Todo("Updated", "Updated", true, Priority.HIGH, null);
		Todo updated = new Todo("Updated", "Updated", true, Priority.HIGH, null);
		updated.setId(1L);
		when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(updated);

		mockMvc
			.perform(put("/api/todos/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateData)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Updated"));
	}

	@Test
	void updateTodo_whenNotExists_shouldReturn404() throws Exception {
		Todo updateData = new Todo();
		when(todoService.updateTodo(eq(99L), any(Todo.class))).thenThrow(new RuntimeException("Todo not found"));

		mockMvc
			.perform(put("/api/todos/99").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateData)))
			.andExpect(status().isNotFound());
	}

	@Test
	void deleteTodo_shouldReturnNoContent() throws Exception {
		doNothing().when(todoService).deleteTodo(1L);

		mockMvc.perform(delete("/api/todos/1")).andExpect(status().isNoContent());
	}

}//