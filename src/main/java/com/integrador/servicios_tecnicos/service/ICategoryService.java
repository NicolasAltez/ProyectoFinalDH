package com.integrador.servicios_tecnicos.service;

import com.integrador.servicios_tecnicos.models.dtos.category.CategoryWithName;
import com.integrador.servicios_tecnicos.models.entity.Category;


import java.util.List;

public interface ICategoryService {
    List<CategoryWithName> getAllCategories();
    Category findCategoryById(Long categoryId);



}
