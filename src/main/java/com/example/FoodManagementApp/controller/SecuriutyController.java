package com.example.FoodManagementApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	 * すでにログイン中の場合は遷移する(/foods)
	 * ***********管理者の/usersへの遷移は未実装***********
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
		if(error != null) {
			mav.addObject("msg", "ログインできませんでした。");
		}
		else {
			mav.addObject("msg", "ユーザー名とパスワードを入力してください");
		}
		return mav;
	}
	
	@PostMapping("/register")
	public ModelAndView register(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, ModelAndView mav) {
		// パスワード暗号化
		String pass = passwordEncoder.encode(password);
		
		// ***** 作成する前に重複チェックと空欄チェックを行うようにする *****
		
		// 空欄の場合はそのままログイン画面にリダイレクト
		if (username.isBlank() || password.isBlank()) {
			mav.setViewName("redirect:/login");
		}
		// 記入したら重複チェックに入る
		else {
			// チェック処理
			
			// ユーザー作成
			User newUser = new User(username, pass, "ROLE_USER");
			repository.save(newUser);
			
			// 暫定遷移先
			mav.setViewName("login");
			mav.addObject("title", "食品管理システム");
			mav.addObject("msg", "登録できました。ログインしてください");
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
	@GetMapping("/foods")
	public ModelAndView foods(ModelAndView mav, HttpServletRequest request) {
//		String user = request.getRemoteUser();
		mav.setViewName("foods");
		mav.addObject("title", "食品管理システム | 食品一覧");
		mav.addObject("title2", "食品一覧");
		mav.addObject("msg", "食品一覧ページ");
		
		return mav;
	}
	
	/**
	 * ユーザー一覧ページ(管理者のみアクセス可能)
	 * @param mav
	 * @param request
	 * @return
	 */
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView users(ModelAndView mav, HttpServletRequest request) {
//		String user = request.getRemoteUser();
		mav.setViewName("users");
		mav.addObject("title", "食品管理システム | ユーザー一覧");
		mav.addObject("title2", "ユーザー一覧");
		mav.addObject("msg", "ユーザー管理ページ");
		
		return mav;
	}
}
