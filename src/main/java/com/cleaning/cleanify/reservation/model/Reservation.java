package com.cleaning.cleanify.reservation.model;


import com.cleaning.cleanify.additionalService.model.AdditionalServices;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.cleaningType.model.CleaningType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "RESERVATIONS")
@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "DATE_TIME")
	private LocalDateTime date;

	@Column(name = "NUMBER_OF_ROOMS")
	private int bedrooms;

	@Column(name = "NUMBER_OF_BATHROOMS")
	private int bathrooms;

	@Column(name = "COMMENT")
	private String additionalInstructions;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "ESTIMATED_TIME_HOURS")
	private BigDecimal estimatedTimeHours;

	@Column(name = "KEY_LOCATION")
	private String keyLocation;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private State state;

	@Column(name = "FLOOR")
	private int floor;

	@Column(name = "APARTMENT")
	private String apartment;

	@OneToOne
	@JoinColumn(name = "CLEANING_TYPE_ID")
	private CleaningType cleaningType;


	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;

	@Column(name = "CLEANING_FREQUENCY")
	private Integer cleaningFrequency;

	@Column(name = "IS_REGULAR_CLEANING")
	private boolean isRegularCleaning;

	@Column(name = "NEXT_CLEANING_DATE")
	private LocalDateTime nextCleaningDate;

	@ManyToMany
	@JoinTable(
			name = "RESERVATION_ADDITIONAL_SERVICE",
			joinColumns = @JoinColumn(name = "RESERVATION_ID"),
			inverseJoinColumns = @JoinColumn(name = "ADDITIONAL_SERVICE_ID")
	)
	private List<AdditionalServices> additionalServices;

	public Reservation() {
	}


	public LocalDateTime getNextCleaningDate() {
		return nextCleaningDate;
	}

	public void setNextCleaningDate(LocalDateTime nextCleaningDate) {
		this.nextCleaningDate = nextCleaningDate;
	}

	public Integer getCleaningFrequency() {
		return cleaningFrequency;
	}

	public void setCleaningFrequency(Integer cleaningFrequency) {
		this.cleaningFrequency = cleaningFrequency;
	}

	public boolean getRegularCleaning() {
		return isRegularCleaning;
	}

	public void setRegularCleaning(boolean regularCleaning) {
		isRegularCleaning = regularCleaning;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String  apartment) {
		this.apartment = apartment;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getAdditionalInstructions() {
		return additionalInstructions;
	}

	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
	}

	public List<AdditionalServices> getAdditionalServices() {
		return additionalServices;
	}

	public void setAdditionalServices(List<AdditionalServices> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public CleaningType getCleaningType() {
		return cleaningType;
	}

	public void setCleaningType(CleaningType cleaningType) {
		this.cleaningType = cleaningType;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getEstimatedTimeHours() {
		return estimatedTimeHours;
	}

	public void setEstimatedTimeHours(BigDecimal estimatedTimeHours) {
		this.estimatedTimeHours = estimatedTimeHours;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyLocation() {
		return keyLocation;
	}

	public void setKeyLocation(String keyLocation) {
		this.keyLocation = keyLocation;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
