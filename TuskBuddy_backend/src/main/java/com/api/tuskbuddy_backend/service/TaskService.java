package com.api.tuskbuddy_backend.service;

import com.api.tuskbuddy_backend.entity.Task;
import com.api.tuskbuddy_backend.exception.TaskNotFoundException;
import com.api.tuskbuddy_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findByOwnerId(String ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }

    public Task findById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task update(String id, Task taskDetail) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskDetail.setId(id);
        return taskRepository.save(taskDetail);
    }

    public void delete(String id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContaining(title);
    }

    public List<Task> searchTasksByOwnerAndTitle(String ownerId, String title) {
        return taskRepository.findByOwnerIdAndTitleContaining(ownerId, title);
    }
}
