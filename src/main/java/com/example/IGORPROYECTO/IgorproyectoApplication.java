package com.example.IGORPROYECTO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.IGORPROYECTO")
public class IgorproyectoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgorproyectoApplication.class, args);
	}

}
