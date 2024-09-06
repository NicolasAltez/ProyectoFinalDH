package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.models.dtos.category.CategoryWithName;
import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.integrador.servicios_tecnicos.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryWithName> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryWithName.class)).toList();
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

}
