package com.example.FoodManagementApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.FoodManagementApp.model.User;
import com.example.FoodManagementApp.service.UserService;

import jakarta.transaction.Transactional;

@Controller
public class UserController {
	@Autowired
	UserService service;

	//ユーザー一覧表示
	@RequestMapping("/users")
	public ModelAndView index(ModelAndView mav) {

		mav.setViewName("users");
		mav.addObject("isSearch", false);
		mav.addObject("title", "ユーザー管理");

		mav.addObject("data", service.getAllUsers());

		return mav;
	}

	// ダミーデータ生成
//	@PostConstruct
//	public void init() {
//
//		User user1 = new User();
//		user1.setUserId("user1");
//		user1.setPassword("888");
//		service.register(user1);
//
//		User user2 = new User();
//		user2.setUserId("user2");
//		user2.setPassword("777");
//		service.register(user2);
//	}

	// ユーザー削除確認画面
	@RequestMapping(value = "/users/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable Integer id, ModelAndView mav) {
		mav.setViewName("u_delete");
		mav.addObject("title", "ユーザー削除画面");
		mav.addObject("msg", "このユーザーをリストから削除しますか？");
		Optional<User> data = service.findById(id);
		mav.addObject("formModel", data.get());
		return mav;
	}

	// ユーザー削除処理
	@RequestMapping(value = "/users/delete", method = RequestMethod.POST)
	@Transactional
	public ModelAndView remove(@RequestParam Integer id, ModelAndView mav) {
		service.deleteUser(id);
		return new ModelAndView("redirect:/users");
	}

	// ユーザーID検索処理
	@RequestMapping(value = "/users/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String keyword, ModelAndView mav) {
		mav.setViewName("users");
		List<User> list = service.searchUser(keyword);
		mav.addObject("data", list);
		mav.addObject("isSearch", true);

		if (list.isEmpty()) {
			mav.addObject("msg", "該当するユーザーがいません。");
		}
		return mav;
	}
}
