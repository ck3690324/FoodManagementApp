package com.example.FoodManagementApp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FoodManagementApp.model.Food;


@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	public Optional<Food> findById(Long idNum);
	
	
	@Query("SELECT f FROM Food f WHERE " +
	           "f.foodName LIKE %:word% OR " +
	           "f.category LIKE %:word% OR " +
	           "f.memo LIKE %:word%")
	List<Food> findByParam(@Param("word") String param);
	
	

	@Query("SELECT f FROM Food f WHERE f.expirationDate > :fdate ORDER BY f.expirationDate ASC")
	public List<Food> findSafeFoods(@Param("fdate") LocalDateTime date);

	@Query("SELECT f FROM Food f WHERE f.expirationDate <= :fdate ORDER BY f.expirationDate ASC")
	public List<Food> findExpiredFoods(@Param("fdate") LocalDateTime date);
}
