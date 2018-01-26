package com.springbootproduct;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.springbootproduct.model.Product;
import com.springbootproduct.repository.ProductRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void testDbInitialization() {
		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(3);
	}

	@Test
	public void testAddProduct() {

		Product product = new Product("Laptop", 10);
		this.entityManager.persist(product);

		Product productFromDB = productRepository.findOne(product.getId());
		assertThat(productFromDB).hasFieldOrPropertyWithValue("name", "Laptop");
		assertThat(productFromDB).hasFieldOrPropertyWithValue("quantity", 10);
	}

	@Test
	public void testFindProductByName() {

		Product productFromDB = productRepository.findByName("Monitor");
		assertThat(productFromDB).hasFieldOrPropertyWithValue("name", "Monitor");
		assertThat(productFromDB).hasFieldOrPropertyWithValue("quantity", 35);
	}

	@Test
	public void testUpdatePoduct() {

		Product productFromDB = productRepository.findByName("Mouse");
		productFromDB.setQuantity(80);
		productRepository.save(productFromDB);
		
		Product updatedProduct = productRepository.findOne(productFromDB.getId());
		assertThat(updatedProduct).hasFieldOrPropertyWithValue("quantity", 80);
	}

	@Test
	public void testDeleteProduct() {

		Product productFromDB = productRepository.findByName("Keyboard");
		productRepository.delete(productFromDB);
		
		Product deletedProduct = productRepository.findOne(productFromDB.getId());
		assertThat(deletedProduct).isNull();
	}

	@Test
	public void testAddProductWithNameBlank() {

		Product product = new Product(" ", 100);
		try {
			this.entityManager.persist(product);
		} 
		catch (ConstraintViolationException e) {
            ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getConstraintDescriptor().getAnnotation().annotationType()).isEqualTo(NotBlank.class);
		}
	}

	@Test
	public void testAddProductWithInvalidNameMaxSize() {

		Product product = new Product("abababbababababababababababababababababbababABABA", 100);
		try {
			this.entityManager.persist(product);
		} 
		catch (ConstraintViolationException e) {
            ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getConstraintDescriptor().getAnnotation().annotationType()).isEqualTo(Size.class);
		}
	}

	@Test
	public void testAddProductWithInvalidMinQuantity() {

		Product product = new Product("Test", -1);
		try {
			this.entityManager.persist(product);
		} 
		catch (ConstraintViolationException e) {
            ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
            assertThat(violation.getConstraintDescriptor().getAnnotation().annotationType()).isEqualTo(Min.class);
		}
	}

}
