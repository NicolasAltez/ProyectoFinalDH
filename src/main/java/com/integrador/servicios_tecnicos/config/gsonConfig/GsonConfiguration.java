package com.integrador.servicios_tecnicos.config.gsonConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Configuration
public class GsonConfiguration {

    @Bean
    public GsonBuilder gsonBuilder() {
        return new GsonBuilder().setPrettyPrinting();
    }

    @Bean
    public Gson gson(){
        return gsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
