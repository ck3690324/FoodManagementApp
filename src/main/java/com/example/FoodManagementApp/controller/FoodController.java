package com.example.FoodManagementApp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.FoodManagementApp.model.Food;
import com.example.FoodManagementApp.repository.FoodRepository;
import com.example.FoodManagementApp.service.FoodService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;




@Controller
public class FoodController {
	
	@Autowired
	FoodRepository repository;
	
	@Autowired
	private FoodService foodService;
	
	@RequestMapping("/foods")
	public ModelAndView foods(@ModelAttribute("formModel") Food food, ModelAndView mav) {
		mav.setViewName("view");
		mav.addObject("title", "Food Page:userId");
		mav.addObject("msg", "This is user page.");
		List<Food> list = repository.findAll();
		
		mav.addObject("formModel", food);
		mav.addObject("data", list);

		ArrayList<String> fieldNames = foodService.generateFieldNames();
		ArrayList<String> fieldJapaneseNames = foodService.generateJapaneseFieldNames();

		mav.addObject("fieldNames", fieldNames);
		mav.addObject("fieldJapaneseNames", fieldJapaneseNames);
		return mav;
	}
	
	
	@RequestMapping(value = "/foods/add", method = RequestMethod.GET)
	public ModelAndView showAddFood(@ModelAttribute("formModel") Food food,ModelAndView mav) {
		

		mav.setViewName("add");
		
		mav.addObject("title", "Add Page ");
		mav.addObject("msg", "Food Add Page ");
		mav.addObject("formModel", food);

		mav.addObject("fieldNames", foodService.generateFieldNames());
		mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());
		return mav;
	}
	
	
	@RequestMapping(value = "/foods/add", method = RequestMethod.POST)
	@Transactional
	public ModelAndView excuteAddFood(@ModelAttribute("formModel") @Validated Food food, BindingResult result,
			ModelAndView mav) {

		ModelAndView res = null;
		System.out.println(result.getFieldErrors());
		if (!result.hasErrors()) {
			repository.saveAndFlush(food);
			res = new ModelAndView("redirect:/foods");
		} else {//バリデーション結果表示
			mav.setViewName("add");
			mav.addObject("title", "Add Page (error)");
			mav.addObject("msg", "sorry, error is occurred...");

			mav.addObject("fieldNames", foodService.generateFieldNames());
			mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());

			res = mav;
		}
		return res;
	}
	
	@RequestMapping(value = "/foods/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editFood(@ModelAttribute("formModel") Food food, @PathVariable int id, ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title", "edit Food.");
		Optional<Food> data = repository.findById((long) id);
		mav.addObject("formModel", data.get());
		mav.addObject("fieldNames", foodService.generateFieldNames());
		mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());
		return mav;
	}
	
	
	
	@RequestMapping(value = "/foods/update", method = RequestMethod.POST)
	@Transactional
	public ModelAndView updateFood(@ModelAttribute("formModel") @Validated Food food, BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		System.out.println(result.getFieldErrors());
		if (!result.hasErrors()) {
			System.out.println("UPDATE");
			repository.saveAndFlush(food);
			res = new ModelAndView("redirect:/foods"); //更新完了処理
		} else {//バリデーション結果表示
			mav.setViewName("edit");
			mav.addObject("title", "Edit Page (error)");
			mav.addObject("msg", "sorry, error is occurred...");
			
			mav.addObject("formModel", food);
			mav.addObject("fieldNames", foodService.generateFieldNames());
			mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());
			res = mav;
		}
		return res;
	}

	
	@RequestMapping(value = "/foods/delete/{id}", method = RequestMethod.GET)
	public ModelAndView showDeleteFood(@PathVariable int id, ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title", "Delete Food.");
		mav.addObject("msg", "Can I delete this record?");
		Optional<Food> data = repository.findById((long) id);
		mav.addObject("formModel", data.get());
		mav.addObject("fieldNames", foodService.generateFieldNames());
		mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());
		return mav;
	}

	@RequestMapping(value = "/foods/delete", method = RequestMethod.POST)
	@Transactional
	public ModelAndView excuteDeleteFood(@RequestParam long id, @RequestHeader(value = "referer", required = false) String referer,ModelAndView mav) {
		repository.deleteById(id);
		System.out.println(referer);
		return new ModelAndView("redirect:/foods");
		
	}
	
	
	
	
	
	@RequestMapping(value = "/foods/search", method = RequestMethod.POST)
	public ModelAndView searchFoods(HttpServletRequest request, ModelAndView mav) {
		
		mav.setViewName("view"); //処理後view画面に遷移」
		String param = request.getParameter("find_str");
		String dateParam = request.getParameter("find_datetime");
		mav.addObject("keywordValue", param);       // キーワード文字列
		mav.addObject("datetimeValue", dateParam);
		
		
		LocalDateTime findDatetime = null;
		String formattedDate=null;

		// dateParam が null でも空文字 "" でも無いときだけパースする！
		if (dateParam != null && !dateParam.trim().isEmpty()) {
		    findDatetime = LocalDateTime.parse(dateParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		 // Formatterで変換=>結局使わない
		    DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		    formattedDate = findDatetime.format(displayFormat);
		    System.out.println(formattedDate);
			
		}
		
		
		
		
		
		boolean hasWord = !param.isEmpty();
		boolean hasDate = (formattedDate != null);
		
		List<Food> data=new ArrayList<Food>();//結果を返すリスト
		
		if (hasWord && hasDate) {
		    // 両方入力されている場合はキーワードで検索→　AND検索できるようにする 
		    //data = foodService.findFoodsAdvanced(param, formattedDate);
			data = foodService.findFoods(param); //1単語のみ対応
		    mav.addObject("data", data);
		} else if (hasWord) {
		    // キーワードのみ
		    data = foodService.findFoods(param);
		    mav.addObject("data", data);
		} else if (hasDate) {
		    // 日時のみ
		    //data = repository.findByDate(formattedDate);
		    
		 // 🟢 1. 期限内のデータをデータベースから直接取得
		    List<Food> safeList = repository.findSafeFoods(findDatetime);

		    // 🔴 2. 期限切れのデータもデータベースから直接取得
		    List<Food> expiredList = repository.findExpiredFoods(findDatetime);

		    // 3. それぞれ画面に送る
		    mav.addObject("safeList", safeList);       // 🟢 期限内
		    mav.addObject("expiredList", expiredList); // 🔴 期限切れ
		    
		    
		} else {
		    mav = new ModelAndView("redirect:/foods");
		}
		

		mav.addObject("title", "Find result");
		mav.addObject("msg", "検索結果");
		mav.addObject("value", param);
		
		mav.addObject("fieldNames", foodService.generateFieldNames());
		mav.addObject("fieldJapaneseNames", foodService.generateJapaneseFieldNames());
		
		
		
		return mav;
	
	}
	
}
