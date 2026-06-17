package com.example.FoodManagementApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.FoodManagementApp.model.User;
import com.example.FoodManagementApp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository repository;
	
	// ユーザー全件取得
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	// ユーザー登録
	public User register(User user) {
		user.setAuthority("ROLE_USER");
		return repository.save(user);
	}
	
	// ユーザー削除
	public void deleteUser(int id) {
		repository.deleteById(id);
	}
	
	// ユーザー検索
	public List<User> searchUser(String keyword) {
		Optional<User> user = repository.findByUserId(keyword);
		
		if (user.isPresent()) {
			return List.of(user.get());
		}
		return List.of();
	}
	
	// ユーザーID(主キー)検索
	public Optional<User> findById(int id){
		return repository.findById(id);
	}
	
	// ユーザー検索(ログイン用)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt = repository.findByUserId(username);
		
		User user = userOpt.orElseThrow(() -> 
		new UsernameNotFoundException("ユーザーが見つかりません: " + username)
	);
		
		return org.springframework.security.core.userdetails.User.withUsername(user.getUserId())
				.password(user.getPassword())
				.authorities(new SimpleGrantedAuthority(user.getAuthority()))
				.build();
	}
}
