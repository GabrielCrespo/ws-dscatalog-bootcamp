package com.gabriel.dscatalog.repositories;

import com.gabriel.dscatalog.entities.Product;
import com.gabriel.dscatalog.factory.Factory;
import com.gabriel.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long totalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000;
        totalProducts = 25;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowsExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product = Factory.createProduct();

        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(totalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotEmptyOptionalWhenIdExists() {

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());

    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {

        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());

    }

}
