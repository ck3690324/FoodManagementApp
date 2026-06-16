package com.example.FoodManagementApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FoodManagementApp.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserId(String userId);
}
