package com.employee.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.login.entity.Task;
import com.employee.login.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> getTasksByUserId(Long userId) {
		return taskRepository.findByUserId(userId);
	}

	public void addTask(Task task) {
		taskRepository.save(task);
	}

}
