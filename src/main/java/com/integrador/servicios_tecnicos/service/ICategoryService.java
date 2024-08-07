package com.integrador.servicios_tecnicos.service;

import com.integrador.servicios_tecnicos.models.dtos.category.CategoryWithName;


import java.util.List;

public interface ICategoryService {
    List<CategoryWithName> getAllCategories();

}
