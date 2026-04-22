package com.luckermt.todo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luckermt.todo.Model.Todo;
import com.luckermt.todo.Repository.TodoRepository;

@Service
public class TodoService {

	@Autowired
	private TodoRepository todoRepository;

	public List<Todo> getAllTodos() {
		return todoRepository.findAllByOrderByPriorityAscDeadlineAsc();
	}

	public Optional<Todo> getTodoById(Long id) {
		return todoRepository.findById(id);
	}

	public Todo createTodo(Todo todo) {
		return todoRepository.save(todo);
	}

	public Todo updateTodo(Long id, Todo updatedTodo) {
		return todoRepository.findById(id).map(todo -> {
			todo.setTitle(updatedTodo.getTitle());
			todo.setDescription(updatedTodo.getDescription());
			todo.setCompleted(updatedTodo.isCompleted());
			todo.setPriority(updatedTodo.getPriority());
			todo.setDeadline(updatedTodo.getDeadline());
			return todoRepository.save(todo);
		}).orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
	}

	public void deleteTodo(Long id) {
		todoRepository.deleteById(id);
	}

}