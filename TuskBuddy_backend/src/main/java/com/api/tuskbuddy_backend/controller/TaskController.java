package com.api.tuskbuddy_backend.controller;

import com.api.tuskbuddy_backend.entity.Task;
import com.api.tuskbuddy_backend.entity.User;
import com.api.tuskbuddy_backend.exception.ForbiddenException;
import com.api.tuskbuddy_backend.exception.TaskNotFoundException;
import com.api.tuskbuddy_backend.service.TaskService;
import com.api.tuskbuddy_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(taskService.findByOwnerId(user.getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByTitle(@RequestParam String title,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(taskService.searchTasksByOwnerAndTitle(user.getId(), title));
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        task.setOwnerId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody Task taskDetail,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Task existing = taskService.findById(id);
        if (!existing.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to update this task");
        }
        return ResponseEntity.ok(taskService.update(id, taskDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Task existing = taskService.findById(id);
        if (!existing.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to delete this task");
        }
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
