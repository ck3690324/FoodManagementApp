package com.example.FoodManagementApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.FoodManagementApp.model.User;
import com.example.FoodManagementApp.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecuriutyController {
//	@Autowired
//	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * ルート(ログインページ)
	 * 自動で/loginへリダイレクトする
	 * @return リダイレクト先(/login)
	 */
	@GetMapping("/")
	public String index() {
		return "redirect:/login";
	}
	
	/**
	 * ログイン処理、ログインページの表示(未ログイン)
	 * すでにログイン中の場合は遷移する(/foods、/users)
	 * @param mav
	 * @param error エラー
	 * @return ビュー
	 */
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView mav, @RequestParam(value="error", required=false) String error, HttpServletRequest request) {
		// すでにログイン中の処理
		if (request.getRemoteUser() != null) {
			// 管理者の遷移先
			if(request.isUserInRole("ADMIN")) {
				mav.setViewName("redirect:/users");
				return mav;
			}
			// 一般ユーザーの遷移先
			mav.setViewName("redirect:/foods");
			return mav;
		}
		
		// 初期アクセス/未ログイン→ログインページへの処理
		mav.setViewName("login");
		mav.addObject("title", "食品管理システム");
		mav.addObject("test", true);
		if(error != null) {
			mav.addObject("msg", "ログインできませんでした。");
		}
		else {
			mav.addObject("msg", "ユーザー名とパスワードを入力してください");
		}
		return mav;
	}
	
	/**
	 * ユーザー登録
	 * 登録時に空欄と重複ユーザーIDのチェックを行う
	 * @param username 入力したユーザーid(ユーザー名)
	 * @param password 入力したパスワード(送信時ハッシュ化)
	 * @param request フォーム送信リクエスト
	 * @param mav ビュー
	 * @return 送信結果
	 */
	@PostMapping("/register")
	public ModelAndView register(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, ModelAndView mav) {
		// パスワード暗号化
		String pass = passwordEncoder.encode(password);
		
		// 空欄の場合はそのままログイン画面にリダイレクト
		if (username.isBlank() || password.isBlank()) {
			mav.setViewName("redirect:/login");
		}
		// 記入したら重複チェックに入る
		else {
			// 弾かれる場合(findByUserIdがemptyの場合)
			if (!repository.findByUserId(username).isEmpty()) {
				mav.setViewName("login");
				mav.addObject("title", "食品管理システム");
				mav.addObject("msg", "IDが重複しています");
			}
			// 登録成功の場合
			else {
				// ユーザー作成
				User newUser = new User(username, pass, "ROLE_USER");
				repository.save(newUser);
								
				// ログインを試す
				try {
					request.login(username, password);
					
					// リダイレクト
					mav.setViewName("redirect:/");
				}
				// ログイン失敗
				catch(Exception e) {
					e.printStackTrace();
					
					mav.addObject("msg", "手動でログインしてください");
					mav.setViewName("redirect:/login");
				}
			}
		}
		// 遷移先を返す
		return mav;
	}
	
	/**
	 * 食品一覧ページ
	 * @param mav
	 * @param request
	 * @return
	 */
//	@GetMapping("/foods")
//	public ModelAndView foods(ModelAndView mav, HttpServletRequest request) {
//		mav.setViewName("foods");
//		mav.addObject("title", "食品管理システム | 食品一覧");
//		mav.addObject("title2", "食品一覧");
//		mav.addObject("msg", "食品一覧ページ");
//		
//		return mav;
//	}
	
}
