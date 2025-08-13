package com.app2.productsCatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app2.productsCatalog.domain.chart.Chart;
import com.app2.productsCatalog.domain.chart.CreateChartDTO;
import com.app2.productsCatalog.domain.products.CreateProductDTO;
import com.app2.productsCatalog.domain.products.Products;
import com.app2.productsCatalog.repository.ChartRepository.UserSummary;
import com.app2.productsCatalog.service.ProductsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductsController {
	@Autowired
	private ProductsService service;
	
	@PostMapping("/add")
	public ResponseEntity createProduct(@RequestBody @Valid CreateProductDTO data) {
		return service.addProduct(data);
	}
	
	@GetMapping
	public List<Products> produtcsReview() {
		return service.showProducts();
	}
	
    @PostMapping("/chart/add")
	public ResponseEntity addChart(@RequestBody @Valid CreateChartDTO data) {
		return service.addProductsInChart(data);

	}
    
    @GetMapping("/clients")
    public List<UserSummary> clientsReview(){
    	return service.showClients();
    }
	
}
