package com.springbootproduct.service;

import java.util.List;

import com.springbootproduct.model.Product;

public interface ProductService {

	List<Product> getAllProducts();

	Product findProductById(Long id);
	
	Product findProductByName(String name);

	void addProduct(Product product);

	void updateProduct(Long id, Product product);
	
	void deleteProduct(Long id);
    
}
