package com.gabriel.dscatalog.factory;

import com.gabriel.dscatalog.dto.ProductDTO;
import com.gabriel.dscatalog.entities.Category;
import com.gabriel.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.00, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory() {
        Category category = new Category(1L, "Electronics");
        return category;
    }

}
