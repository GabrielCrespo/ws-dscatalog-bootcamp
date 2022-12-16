package com.gabriel.dscatalog.services;

import com.gabriel.dscatalog.dto.ProductDTO;
import com.gabriel.dscatalog.entities.Product;
import com.gabriel.dscatalog.repository.ProductRepository;
import com.gabriel.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest request) {
        return productRepository.
                findAll(request)
                .map(product -> new ProductDTO(product, product.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return new ProductDTO(product, product.getCategories());
    }

}
