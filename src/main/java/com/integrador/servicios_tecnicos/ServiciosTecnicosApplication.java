package com.integrador.servicios_tecnicos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiciosTecnicosApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiciosTecnicosApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiciosTecnicosApplication.class, args);
		LOGGER.info("Application running, ejemplo de pr");
	}

}
