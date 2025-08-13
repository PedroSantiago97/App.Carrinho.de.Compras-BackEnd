package com.app2.productsCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app2.productsCatalog.domain.products.Products;

public interface ProductsRepository extends JpaRepository<Products, String>{
	Products findByName(String name);
}
