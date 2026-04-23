package com.luckermt.todo.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo {
public static final int maxSize = 100;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;
	
	private boolean completed;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	private LocalDateTime deadline;

	public Todo() {
	}

	public Todo(String title, String description, boolean completed, Priority priority, LocalDateTime deadline) {
		this.title = title;
		this.description = description;
		this.completed = completed;
		this.priority = priority;
		this.deadline = deadline;
	}

	public Long getId() {
		return id;
	}
	public void doSomething( String param ){}  // пробел после '(' и перед ')'
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

}