package com.example.FoodManagementApp.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.FoodManagementApp.model.Food;
import com.example.FoodManagementApp.repository.FoodRepository;

import jakarta.persistence.Column;



@Service
public class FoodService {
	
	public String getLoginUserId() {
        return SecurityContextHolder.getContext()
                                     .getAuthentication()
                                     .getName();
    }
	
	
	
	public ArrayList<String> generateFieldNames() {
		Field[] allFields = Food.class.getDeclaredFields();

		// 🌟 ただの文字列（String）を入れるArrayListを用意
		ArrayList<String> fieldNames = new ArrayList<>();

		for (Field field : allFields) {
			String fieldName = field.getName();

			// id は画面に入力させないのでスキップ
			if (fieldName.equals("id")) {
				continue;
			}

			// 🌟 見つかった変数名を、そのまま文字として add するだけ！
			fieldNames.add(fieldName);
		}

		return fieldNames;
	}

	public ArrayList<String> generateJapaneseFieldNames() {
		Field[] allFields = Food.class.getDeclaredFields();
		ArrayList<String> japaneseNames = new ArrayList<>();

		for (Field field : allFields) {
			String fieldName = field.getName();

			// id は画面に入力させないのでスキップ
			if (fieldName.equals("id")) {
				continue;
			}

			// 🌟 ① まずデフォルトとして「英語の変数名」を入れておく
			String japaneseName = fieldName;

			// @Column から日本語名（comment）を取得
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				// 🌟 ② comment属性が空（""）じゃない場合「だけ」日本語名に上書き！
				if (!column.comment().isEmpty()) {
					japaneseName = column.comment();
				}
			}

			// リストに追加して次のフィールドへ
			japaneseNames.add(japaneseName);
		}
		return japaneseNames;
	}
	
	
	@Autowired FoodRepository foodRepository;
	public List<Food> findFoods(String param) {//singleKeyword
		
		Long fid = 0L;
		List<Food> results=new ArrayList<Food>();
		param = param.trim();
		
		try {
			fid = Long.parseLong(param);
			Optional<Food> result=foodRepository.findById(fid); //ほかの列の数字は部分一致で検索できない
			if (result.isPresent()) {
			    results.add(result.get());
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			
		}
		
		if (results.isEmpty()) {//もしidでヒットしなかったらほかの列でキーワード検索
			results=foodRepository.findByParam(param);
            return results;
        }
		
		
		
		return results;
		
	}

	public List<Food> findByDatetime(String formattedDate) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	
	
	

}
