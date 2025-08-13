package com.app2.productsCatalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app2.productsCatalog.domain.chart.Chart;
import com.app2.productsCatalog.domain.chart.CreateChartDTO;
import com.app2.productsCatalog.domain.products.CreateProductDTO;
import com.app2.productsCatalog.domain.products.Products;
import com.app2.productsCatalog.domain.user.User;
import com.app2.productsCatalog.repository.ChartRepository;
import com.app2.productsCatalog.repository.ChartRepository.UserSummary;
import com.app2.productsCatalog.repository.ProductsRepository;
import com.app2.productsCatalog.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductsRepository repository;
	
	@Autowired
	private ChartRepository chartRepository;
	
	@Transactional
	public ResponseEntity addProduct(CreateProductDTO data) {
		if(this.repository.findByName(data.name()) != null) return ResponseEntity.status(HttpStatus.CONFLICT).body("The Name has Already Exists in other Product!");
		Products newProduct = new Products(data.name(), data.image_url(), data.price());
		
		this.repository.save(newProduct);
		return ResponseEntity.ok().build();
	}
	
	public List<Products> showProducts() {
		List<Products> products = repository.findAll();
		return products;
	}
	
	@Transactional 
	public ResponseEntity addProductsInChart(CreateChartDTO data) {
		UserDetails user = userRepository.findByLogin(data.nome());
		if(user == null) return ResponseEntity.notFound().build();
		
		if(user instanceof User) {
			User userA = (User) user;
			UUID idString = userA.getId();
			Chart newChart = new Chart(idString, data.total_value(), data.qtd_itens());
			chartRepository.save(newChart);
			return ResponseEntity.ok().build();
		}
	
		return ResponseEntity.badRequest().build();	
	}
	
	@Transactional
	public List<UserSummary> showClients(){
		List<UserSummary> charts = chartRepository.getUserPurchaseSummary();

		return charts;
	}

}
