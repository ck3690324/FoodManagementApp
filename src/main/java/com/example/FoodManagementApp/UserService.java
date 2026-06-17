package com.example.FoodManagementApp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	UserRepository repository;

	public List<User> getAllUsers() {
		return repository.findAll();
	}

	public User register(User user) {

		user.setAuthority("ROLE_USER");

		return repository.save(user);
	}

	public void deleteUser(Integer id) {
		repository.deleteById(id);
	}

	public List<User> searchUser(String keyword) {
		Optional<User> user = repository.findByUserId(keyword);

		if (user.isPresent()) {
			return List.of(user.get());
		}
		return List.of();
	}
	
	public Optional<User> findById(Integer id){
		return repository.findById(id);
	}
}
