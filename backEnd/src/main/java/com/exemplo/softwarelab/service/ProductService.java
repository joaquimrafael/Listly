package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Product;
import com.exemplo.softwarelab.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductService {
	@Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product updatedList) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
        	Product product = existingProduct.get();
        	product.setName(updatedList.getName());
            return productRepository.save(product);
        }
        throw new RuntimeException("Produto não encontrado com ID: " + id);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
        	productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
    }
}
