package com.cleaning.cleanify.cleaningType.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Table(name = "CLEANING_TYPE")
@Entity
public class CleaningType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "PRICE")
	private BigDecimal price;

	public CleaningType() {
	}

	public CleaningType(Long id, String key, BigDecimal price, String value) {
		this.id = id;
		this.key = key;
		this.price = price;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getValue() {
		return value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
