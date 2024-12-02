package com.cleaning.cleanify.commercial.model;


import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.reservation.model.State;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMERCIAL_CLEANING")
public class CommercialCleaning {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "ADDRESS", nullable = false)
	private String address;

	@Column(name = "DATE_TIME", nullable = false)
	private LocalDateTime dateTime;

	@Column(name = "FLOOR", nullable = false)
	private Integer floor;

	@Column(name = "SQUARE_FT", nullable = false)
	private Float squareFt;

	@Column(name = "COMPANY_NAME", nullable = false)
	private String companyName;

	@Column(name = "PHONE_NUMBER", nullable = false)
	private String phoneNumber;

	@Column(name = "CONTACT_PERSON_NAME", nullable = false)
	private String contactPersonName;

	@Column(name = "ADDITIONAL_INSTRUCTIONS")
	private String additionalInstructions;

	@Column(name = "PRICE", nullable = true)
	private BigDecimal price;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private State confirmed;

	@Column(name = "CREATED_AT", nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	public CommercialCleaning() {
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public State getConfirmed() {
		return confirmed;
	}

	public void setState(State confirmed) {
		this.confirmed = confirmed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdditionalInstructions() {
		return additionalInstructions;
	}

	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Float getSquareFt() {
		return squareFt;
	}

	public void setSquareFt(Float squareFt) {
		this.squareFt = squareFt;
	}



}
