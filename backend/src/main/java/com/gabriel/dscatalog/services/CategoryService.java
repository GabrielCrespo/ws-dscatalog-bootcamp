package com.gabriel.dscatalog.services;

import com.gabriel.dscatalog.dto.CategoryDTO;
import com.gabriel.dscatalog.entities.Category;
import com.gabriel.dscatalog.repository.CategoryRepository;
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

}
