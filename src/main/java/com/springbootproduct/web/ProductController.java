package com.springbootproduct.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springbootproduct.exception.ProductException;
import com.springbootproduct.model.Product;
import com.springbootproduct.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProductController {

	public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@GetMapping()
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> list = productService.getAllProducts();
		return ResponseEntity.ok(list);
		// return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
	}

	@PostMapping("/product")
	public ResponseEntity<?> findProductByName(@RequestParam("product") String name) throws ProductException {
		Product product = productService.findProductByName(name);
		if (product == null) {
			logger.error("Product with name {} not found.", name);
			throw new ProductException("Product with name " + name + " not found", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(product);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findProductById(@PathVariable Long id) throws ProductException {
		Product product = productService.findProductById(id);
		if (product == null) {
			logger.error("Product with id {} not found.", id);
			throw new ProductException("Product with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(product);
	}

	@PostMapping()
	public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors) throws ProductException {

		if (errors.hasErrors()) {
			throw new ProductException(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}

		boolean productExists = productService.findProductByName(product.getName()) != null;
		if (productExists) {
			logger.error("Unable to create. A product with name {} already exist", product.getName());
			throw new ProductException("Unable to create. A product with name " + product.getName() + " already exist.",
					HttpStatus.CONFLICT);

		}

		productService.addProduct(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, Errors errors)
			throws ProductException {
		if (errors.hasErrors()) {
			throw new ProductException(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		Product currentProduct = productService.findProductById(id);
		if (currentProduct == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			throw new ProductException("Unable to update. Product with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		productService.updateProduct(id, product);

		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws ProductException {
		Product currentProduct = productService.findProductById(id);
		if (currentProduct == null) {
			logger.error("Unable to delete. Product with id {} not found.", id);
			throw new ProductException("Unable to delete. Product with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		productService.deleteProduct(id);
		return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
	}
}
