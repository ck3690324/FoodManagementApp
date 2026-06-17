package com.example.FoodManagementApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@NotNull
	private long id;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "authority", nullable = false)
	private String authority;
	
	// コンストラクタ
	public User() {}
	
	public User(String userId, String password, String authority) {
		super();
		this.userId = userId;
		this.password = password;
		this.authority = authority;
	}

	// ゲッター
	public long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getAuthority() {
		return authority;
	}

	// セッター
	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", password=" + password + ", authority=" + authority + "]";
	}
}
