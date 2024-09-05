package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import com.integrador.servicios_tecnicos.repository.CharacteristicsRepository;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import com.integrador.servicios_tecnicos.service.ICharacteristicsService;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CharacteristicsService implements ICharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;

    public CharacteristicsService(CharacteristicsRepository characteristicsRepository) {
        this.characteristicsRepository = characteristicsRepository;

    }

    @Override
    public List<Characteristic> getAllCharacteristics() {
        return characteristicsRepository.findAll();
    }
}
