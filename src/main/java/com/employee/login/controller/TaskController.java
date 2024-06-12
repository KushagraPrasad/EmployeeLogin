package com.employee.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.employee.login.dto.TaskDto;
import com.employee.login.entity.Task;
import com.employee.login.entity.User;
import com.employee.login.repository.TaskRepository;
import com.employee.login.repository.UserRepository;
import com.employee.login.service.JwtService;
import com.employee.login.service.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("/tasks")
	public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
		String email = jwtService.extractUsername(token.substring(7));
		Long userId = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"))
				.getId();
		List<Task> tasks = taskService.getTasksByUserId(userId);
		return ResponseEntity.ok(tasks);
	}

	@PostMapping("/tasks/add")
	public ResponseEntity<String> addTask(@RequestHeader("Authorization") String token, @RequestBody TaskDto taskDTO) {
		User assignee = userRepository.findByEmail(taskDTO.getAssigneeEmail())
				.orElseThrow(() -> new RuntimeException("Assignee not found"));
		Task task = new Task();
		task.setUser(assignee);
		task.setDescription(taskDTO.getDescription());
		task.setDueDate(taskDTO.getDueDate());
		taskRepository.save(task);
		return ResponseEntity.ok("Task added successfully");
	}
}