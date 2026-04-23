package com.luckermt.todo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luckermt.todo.Model.Priority;
import com.luckermt.todo.Model.Todo;
import com.luckermt.todo.Repository.TodoRepository;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

	@Mock
	private TodoRepository todoRepository;

	@InjectMocks
	private TodoService todoService;

	private Todo todo1;

	private Todo todo2;

	@BeforeEach
	void setUp() {
		todo1 = new Todo("Title1", "Desc1", false, Priority.HIGH, LocalDateTime.of(2026, 4, 23, 12, 0));
		todo1.setId(1L);
		todo2 = new Todo("Title2", "Desc2", true, Priority.LOW, LocalDateTime.of(2026, 4, 24, 15, 0));
		todo2.setId(2L);
	}

	@Test
	void getAllTodos_shouldReturnSortedList() {
		when(todoRepository.findAllByOrderByPriorityAscDeadlineAsc()).thenReturn(List.of(todo1, todo2));

		List<Todo> result = todoService.getAllTodos();

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getPriority()).isEqualTo(Priority.HIGH);
		verify(todoRepository).findAllByOrderByPriorityAscDeadlineAsc();
	}

	@Test
	void getTodoById_whenExists_shouldReturnTodo() {
		when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));

		Optional<Todo> result = todoService.getTodoById(1L);

		assertThat(result).isPresent().contains(todo1);
	}

	@Test
	void getTodoById_whenNotExists_shouldReturnEmpty() {
		when(todoRepository.findById(99L)).thenReturn(Optional.empty());

		Optional<Todo> result = todoService.getTodoById(99L);

		assertThat(result).isEmpty();
	}

	@Test
	void createTodo_shouldSaveAndReturnTodo() {
		Todo newTodo = new Todo("New", "New desc", false, Priority.MEDIUM, LocalDateTime.now());
		when(todoRepository.save(any(Todo.class))).thenReturn(newTodo);

		Todo saved = todoService.createTodo(newTodo);

		assertThat(saved).isEqualTo(newTodo);
		verify(todoRepository).save(newTodo);
	}

	@Test
	void updateTodo_whenExists_shouldUpdateFieldsAndReturn() {
		Todo updatedData = new Todo("Updated", "Updated desc", true, Priority.LOW, LocalDateTime.of(2026, 5, 1, 10, 0));
		when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));
		when(todoRepository.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

		Todo result = todoService.updateTodo(1L, updatedData);

		assertThat(result.getTitle()).isEqualTo("Updated");
		assertThat(result.getDescription()).isEqualTo("Updated desc");
		assertThat(result.isCompleted()).isTrue();
		assertThat(result.getPriority()).isEqualTo(Priority.LOW);
		verify(todoRepository).save(todo1);
	}

	@Test
	void updateTodo_whenNotExists_shouldThrowException() {
		Todo updatedData = new Todo();
		when(todoRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> todoService.updateTodo(99L, updatedData)).isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Todo not found with id 99");
		verify(todoRepository, never()).save(any());
	}

	@Test
	void deleteTodo_shouldCallRepositoryDeleteById() {
		todoService.deleteTodo(1L);
		verify(todoRepository).deleteById(1L);
	}

}