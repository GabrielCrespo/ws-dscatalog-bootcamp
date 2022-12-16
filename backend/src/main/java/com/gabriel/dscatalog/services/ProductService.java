package com.gabriel.dscatalog.services;

import com.gabriel.dscatalog.dto.CategoryDTO;
import com.gabriel.dscatalog.dto.ProductDTO;
import com.gabriel.dscatalog.entities.Category;
import com.gabriel.dscatalog.entities.Product;
import com.gabriel.dscatalog.repository.CategoryRepository;
import com.gabriel.dscatalog.repository.ProductRepository;
import com.gabriel.dscatalog.services.exceptions.DatabaseException;
import com.gabriel.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @Transactional
    public ProductDTO create(ProductDTO dto) {
       Product product = new Product();
       copyDtoToEntity(dto, product);
       product = productRepository.save(product);

       return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(ProductDTO dto, Long id) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, product);
            product = productRepository.save(product);

            return new ProductDTO(product);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImgUrl(dto.getImgUrl());
        product.setDate(dto.getDate());

        product.getCategories().clear();
        for(CategoryDTO categoryDTO : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            product.getCategories().add(category);
        }
    }

}
