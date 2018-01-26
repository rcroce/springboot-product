package com.springbootproduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootproduct.model.Product;
import com.springbootproduct.service.ProductService;
import com.springbootproduct.web.ProductController;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
public class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ProductService productServiceMock;

	@Autowired
	ObjectMapper objectMapper;

	List<Product> products;

	@Before
	public void setUp() throws Exception {

		products = new ArrayList<Product>(
				Arrays.asList(
						new Product("Mouse", 50),
						new Product("Keyboard", 5),
						new Product("Monitor", 35)));
	}

	@Test
    public void getProductsShouldReturnJsonArray() throws Exception {
		
		assertThat(this.productServiceMock).isNotNull();
		
		given(productServiceMock.getAllProducts()).willReturn(products);
    	
    	this.mockMvc
    	.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", is("Mouse")))
        .andExpect(jsonPath("$[0].quantity", is(50)))
        .andExpect(jsonPath("$[1].name", is("Keyboard")))
        .andExpect(jsonPath("$[1].quantity", is(5)))
        .andExpect(jsonPath("$[2].name", is("Monitor")))
        .andExpect(jsonPath("$[2].quantity", is(35)))
        .andDo(print());
    }

	@Test
    public void findProductByName() throws Exception {
		
		assertThat(this.productServiceMock).isNotNull();
		
		Optional<Product> monitor = products.stream().filter(p -> p.getName().equals("Monitor")).findFirst();
		given(productServiceMock.findProductByName("Monitor")).willReturn(monitor.get());
    	
    	this.mockMvc
    	.perform(post("/products/product").param("product", "Monitor").contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.name", is("Monitor")))
        .andExpect(jsonPath("$.quantity", is(35)))
        .andDo(print());
    }

	@Test
    public void testProductNotFound() throws Exception {
		
		assertThat(this.productServiceMock).isNotNull();
		
		Optional<Product> monitor = products.stream().filter(p -> p.getName().equals("Monitor")).findFirst();
		given(productServiceMock.findProductByName("Monitor")).willReturn(monitor.get());
    	
    	this.mockMvc
    	.perform(post("/products/product").param("product", "ProductXYZ").contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isNotFound())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.errorMessage", is("Product with name ProductXYZ not found")))
        .andDo(print());
    }

	@Test
    public void testCreateProduct() throws Exception {
		
		Product product = new Product("Laptop", 10);

		this.mockMvc
    	.perform(post("/products/").content(product.toString()).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isCreated())
        .andDo(print());
    }

	@Test
    public void testProductAlreadyExists() throws Exception {
		
		Product product = new Product("Laptop", 10);

		given(productServiceMock.findProductByName("Laptop")).willReturn(product);

		this.mockMvc
    	.perform(post("/products/").content(product.toString()).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isConflict())
        .andDo(print());
    }

	@Test
    public void testUpdateProduct() throws Exception {
		
		Product product = new Product("Laptop", 10);
		Long id = Long.valueOf(1);
		product.setId(id);
		
		given(productServiceMock.findProductById(id)).willReturn(product);

		this.mockMvc
    	.perform(put("/products/{id}", 1L).content(product.toString()).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
        .andDo(print());
    }

	@Test
    public void testDeleteProduct() throws Exception {
		
		Product product = new Product("Laptop", 10);
		Long id = Long.valueOf(1);
		product.setId(id);
		
		given(productServiceMock.findProductById(id)).willReturn(product);

		this.mockMvc
    	.perform(delete("/products/{id}", id).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isNoContent())
        .andDo(print());
    }

	@Test
    public void testUpdateProductWithInvalidMinQuantity() throws Exception {
		
		Product product = new Product("Laptop", -1);
		Long id = Long.valueOf(1);
		product.setId(id);
		
		given(productServiceMock.findProductById(id)).willReturn(product);

		this.mockMvc
    	.perform(put("/products/{id}", id).content(product.toString()).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isBadRequest())
        .andDo(print());
    }

	@Test
    public void testUpdateProductWithInvalidNameMaxSize() throws Exception {
		
		Product product = new Product("Laptopdjskjdksjdksjkdjskdjskjdksjdksjdkjskdjskdjskj", -1);
		Long id = Long.valueOf(1);
		product.setId(id);
		
		given(productServiceMock.findProductById(id)).willReturn(product);

		this.mockMvc
    	.perform(put("/products/{id}", id).content(product.toString()).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isBadRequest())
        .andDo(print());
    }


}
