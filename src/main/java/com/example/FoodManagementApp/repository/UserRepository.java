package com.example.FoodManagementApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FoodManagementApp.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	// 主キー検索
	Optional<User> findById(int id);
	// ユーザー名(userId)検索
	Optional<User> findByUserIdContaining(String userId);
	Optional<User> findByUserId(String userId);
	// ユーザー削除
	User deleteById(int id);
}
