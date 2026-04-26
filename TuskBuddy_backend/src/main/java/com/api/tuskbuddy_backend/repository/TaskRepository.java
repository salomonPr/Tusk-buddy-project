package com.api.tuskbuddy_backend.repository;

import com.api.tuskbuddy_backend.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByTitleContaining(String title);
    List<Task> findByOwnerId(String ownerId);
    List<Task> findByOwnerIdAndTitleContaining(String ownerId, String title);
}
