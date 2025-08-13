package com.app2.productsCatalog.domain.products;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String name;
	private String image_url;
	private Double price;
	
	public Products(String name, String image_url, Double price) {
		this.image_url = image_url;
		this.name = name;
		this.price = price;
	}
	
}
