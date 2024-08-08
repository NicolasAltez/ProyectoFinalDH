package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.models.dtos.category.CategoryWithName;
import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.repository.CategoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Nested
    @Tag("all categories")
    class getAllCategories {

        @Test
        void shouldReturnAllCategories(){
            when(categoryRepository.findAll()).thenReturn(List.of(Category.builder()
                    .id(1L)
                    .description("category description")
                    .name("category name")
                    .productList(List.of())
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build()));
            List<CategoryWithName> categories = categoryService.getAllCategories();
            assertFalse(categories.isEmpty());
            verify(categoryRepository, times(1)).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoCategories(){
            when(categoryRepository.findAll()).thenReturn(List.of());
            List<CategoryWithName> categories = categoryService.getAllCategories();
            assertTrue(categories.isEmpty());
            verify(categoryRepository, times(1)).findAll();
        }
    }

}
