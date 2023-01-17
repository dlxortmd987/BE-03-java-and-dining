package com.prgms.allen.dining.domain.restaurant.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.springframework.util.Assert;

import com.prgms.allen.dining.domain.customer.entity.Customer;
import com.prgms.allen.dining.domain.customer.entity.CustomerType;

@Entity
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "restaurant_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "customer_id")
	private Customer owner;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "food_type", nullable = false)
	private FoodType foodType;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(name = "capacity", nullable = false)
	private int capacity;

	@Column(name = "open_time", nullable = false)
	private LocalTime openTime;

	@Column(name = "last_order_time", nullable = false)
	private LocalTime lastOrderTime;

	@Column(name = "location", nullable = false)
	private String location;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "phone", length = 11, nullable = false)
	private String phone;

	@ElementCollection
	@CollectionTable(name = "Menu", joinColumns = @JoinColumn(name = "restaurant_id"))
	private List<Menu> menu = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "ClosingDay", joinColumns = @JoinColumn(name = "restaurant_id"))
	private List<ClosingDay> closingDays = new ArrayList<>();

	protected Restaurant() {
	}

	public Restaurant(Customer owner, FoodType foodType, String name, int capacity, LocalTime openTime,
		LocalTime lastOrderTime, String location, String description, String phone, List<Menu> menuList,
		List<ClosingDay> closingDays) {
		this(null, owner, foodType, name, capacity, openTime, lastOrderTime, location, description, phone,
			menuList, closingDays);
	}

	public Restaurant(Long id, Customer owner, FoodType foodType, String name, int capacity, LocalTime openTime,
		LocalTime lastOrderTime, String location, String description, String phone, List<Menu> menuList,
		List<ClosingDay> closingDays) {
		validate(owner, name, capacity, phone);
		this.id = id;
		this.owner = owner;
		this.foodType = foodType;
		this.name = name;
		this.capacity = capacity;
		this.openTime = openTime;
		this.lastOrderTime = lastOrderTime;
		this.location = location;
		this.description = description;
		this.phone = phone;
		this.menu = menuList;
		this.closingDays = closingDays;
	}

	public Long getId() {
		return id;
	}

	public Customer getOwner() {
		return owner;
	}

	public FoodType getFoodType() {
		return foodType;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public LocalTime getLastOrderTime() {
		return lastOrderTime;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public String getPhone() {
		return phone;
	}

	public List<Menu> getMenu() {
		return new ArrayList<>(menu);
	}

	public List<ClosingDay> getClosingDays() {
		return new ArrayList<>(closingDays);
	}

	public boolean isNotOwner(Long requestOwnerId) {
		return !requestOwnerId.equals(
			this.owner
				.getId());
	}

	public List<Menu> getMinorMenu() {
		if (menu.size() < 5) {
			return this.getMenu();
		}
		return this.menu.subList(0, 4);
	}

	public void validate(Customer owner, String name, int capacity, String phone) {
		validateOwnerType(owner);
		validateName(name);
		validateCapacity(capacity);
		validatePhone(phone);
	}

	private void validateOwnerType(Customer owner) {
		Assert.isTrue(CustomerType.OWNER.equals(owner.getCustomerType()), "권한이 없습니다.");
	}

	private void validateName(String name) {
		Assert.isTrue(name.length() > 1, "두 글자 이상 입력해주세요");
		Assert.isTrue(name.length() < 30, "30글자 이내로 입력해주세요");
	}

	private void validateCapacity(int capacity) {
		Assert.isTrue(capacity > 2, "최대 수용 인원은 2명 이상이어야 합니다.");
	}

	private void validatePhone(String phone) {
		Assert.hasLength(phone, "Phone must be not empty.");
		Assert.isTrue(phone.length() >= 9 && phone.length() <= 11, "Name must be between 2 and 5.");
		Assert.isTrue(Pattern.matches("^[0-9]+$", phone), "Phone is invalid format");
	}

}
