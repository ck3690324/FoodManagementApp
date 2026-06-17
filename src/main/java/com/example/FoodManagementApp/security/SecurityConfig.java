package com.example.FoodManagementApp.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * セキュリティフィルターチェーンを設定する。 アクセス可能なURLパスを分ける。
	 * 
	 * @param http セキュリティオブジェクト
	 * @return 設定が適用されたインスタンス
	 * @throws Exception 例外
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http
			.authorizeHttpRequests((request) -> request
				.requestMatchers("/").permitAll()								// 未ログイン/エラーの場合でもアクセス可能
				.requestMatchers("/register").permitAll()						// 新規登録
				.requestMatchers("/js/**", "/css/**", "/img/**").permitAll()	// デフォルトアクセス可能パス(ユーザー、管理者)
				.requestMatchers("/error").permitAll()							// エラーでも表示を許可する
				.requestMatchers("/users").hasRole("ADMIN")						// 管理者のみアクセス可能パス
				.anyRequest().authenticated()
			)
			// ログイン
			.formLogin((form) -> form
				.loginProcessingUrl("/login")									// ログイン処理のURLパス
				.loginPage("/login")											// ログインページのURLパス
				.defaultSuccessUrl("/login")									// デフォルト遷移先
				.permitAll()
			)
			// ログアウト
			.logout((logout) -> logout
				.logoutSuccessUrl("/login").permitAll()							// ログアウト後の遷移先
			);
		
		return http.build();
	}

	/**
	 * パスワード暗号化
	 * @return エンコーダー
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
//	/**
//	 * ログイン処理用
//	 * serviceにあるものと同じ
//	 * @param dataSource
//	 * @return
//	 */
//	@Bean
//	public UserDetailsService userDetailsService(DataSource dataSource) {
//		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//		
//		manager.setUsersByUsernameQuery(
//			"SELECT user_id as username, password, true FROM users WHERE user_id = ?"
//		);
//
//		manager.setAuthoritiesByUsernameQuery(
//			"SELECT user_id as username, authority FROM users WHERE user_id = ?"
//		);
//		
//		return manager;
//	}
	
//	/**
//	 * アカウント作成→mySQL
//	 * @param passwordEncoder
//	 * @return
//	 */
//	@Bean
//	public CommandLineRunner initDummyAccounts(PasswordEncoder passwordEncoder) {
//		return args -> {
//			String countSql = "SELECT COUNT(*) FROM users";
//			Integer userCount = jdbcTemplate.queryForObject(countSql, Integer.class);
//			
//			if (userCount != null && userCount == 0) {
//				String insertSql = "INSERT INTO users (id, user_id, password, authority) VALUES (?, ?, ?, ?)";
//				
//				String adminPass = passwordEncoder.encode("pass");
//				jdbcTemplate.update(insertSql, 1, "admin", adminPass, "ROLE_ADMIN");
//				
//				String userPass = passwordEncoder.encode("pass");
//				jdbcTemplate.update(insertSql, 2, "user", userPass, "ROLE_USER");
//				
//				System.out.println("アカウント（admin, user）を作成しました。");
//			}
//			else {
//				System.out.println("アカウント作成しませんでした");
//			}
//		};
//	}
	
//	/**
//	 * ダミーアカウント作成(メモリ)
//	 * 
//	 * @param passwordEncoder ハッシュ化エンコーダー
//	 * @return アカウント
//	 */
//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
//		// ダミーのユーザーアカウント
//		UserDetails user = User.withUsername("user").password(passwordEncoder.encode("pass")).roles("USER").build();
//
//		// ダミーの管理者アカウント
//		UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("pass")).roles("ADMIN").build();
//
//		return new InMemoryUserDetailsManager(user, admin);
//	}
}
