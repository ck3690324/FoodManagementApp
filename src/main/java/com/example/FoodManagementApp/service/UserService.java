package com.example.FoodManagementApp.service;

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
	private UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUserId(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
		}
		
		return org.springframework.security.core.userdetails.User.withUsername(user.getUserId())
				.password(user.getPassword())
				.authorities(new SimpleGrantedAuthority(user.getAuthority()))
				.build();
	}
}
