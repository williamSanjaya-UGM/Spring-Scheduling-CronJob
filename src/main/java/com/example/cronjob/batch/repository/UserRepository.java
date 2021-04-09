package com.example.cronjob.batch.repository;

import com.example.cronjob.batch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
