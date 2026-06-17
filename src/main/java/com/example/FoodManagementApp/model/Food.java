package com.example.FoodManagementApp.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "foods")
public class Food {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//private String productName;

	@Column(name = "foodName", comment = "食品名",nullable = false)
	@NotBlank(message = "商品名を入力して下さい")
	private String foodName;

	@Column(name = "remains", comment = "残量")
	private String remains = "未開封"; // 初期値
	
    //@ManyToOne //userIDを外部キーとする
	@Column(name = "purchaser", comment = "購入者")
	private String userID;
    
    @Column(name="puchaseDate", comment="購入日")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date purchaseDate;

	@Column(name = "expirationDate", comment = "期限")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date expirationDate;
	
	@Column(name = "storingPlace", comment = "保管場所")
	private String storingPlace;
	
	
	@Column(name = "category", comment = "カテゴリ")
	private String category;

	@Column(name = "memo", comment = "メモ")
	private String memo;

	
	
	//getter setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getRemains() {
		return remains;
	}

	public void setRemains(String remains) {
		this.remains = remains;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getStoringPlace() {
		return storingPlace;
	}

	public void setStoringPlace(String storingPlace) {
		this.storingPlace = storingPlace;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	
	
	
}
