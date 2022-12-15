package com.gabriel.dscatalog.services;

import com.gabriel.dscatalog.dto.CategoryDTO;
import com.gabriel.dscatalog.entities.Category;
import com.gabriel.dscatalog.repository.CategoryRepository;
import com.gabriel.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(category -> new CategoryDTO(category))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO create(CategoryDTO dto) {

        Category category = new Category();
        category.setName(dto.getName());

        category = categoryRepository.save(category);

        return new CategoryDTO(category);
    }
}
