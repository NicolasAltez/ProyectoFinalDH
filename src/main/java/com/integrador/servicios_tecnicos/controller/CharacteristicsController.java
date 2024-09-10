package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.models.dtos.category.CategoryWithName;
import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import com.integrador.servicios_tecnicos.service.ICategoryService;
import com.integrador.servicios_tecnicos.service.ICharacteristicsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/characteristics")
public class CharacteristicsController {

    private final ICharacteristicsService characteristicsService;

    public CharacteristicsController(ICharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @GetMapping
    public ResponseEntity<List<Characteristic>> getAllCharacteristics(){
        return new ResponseEntity<>(characteristicsService.getAllCharacteristics(), HttpStatus.OK);
    }
}
