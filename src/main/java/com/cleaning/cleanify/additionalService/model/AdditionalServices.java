package com.cleaning.cleanify.additionalService.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ADDITIONAL_SERVICE")
public class AdditionalServices {

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

	public AdditionalServices() {
	}

	public AdditionalServices(Long id, String key, BigDecimal price, String value) {
		this.id = id;
		this.key = key;
		this.price = price;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
