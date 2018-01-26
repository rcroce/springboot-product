package com.springbootproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbootproduct.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Product findByName(String name);

}
