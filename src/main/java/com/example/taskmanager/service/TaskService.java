
package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));
    }

    public Task addTask(String title) {
        Task task = new Task(null, title, false);
        return taskRepository.save(task);
    }

    public Task updateTask(
            Long id, String title, boolean completed) {

        Task task = getTaskById(id);
        task.setTitle(title);
        task.setCompleted(completed);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    public Page<Task> getTasks(
            Boolean completed,
            String keyword,
            Pageable pageable) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        return taskRepository.searchTasks(
                completed, keyword, pageable);
    }

}
