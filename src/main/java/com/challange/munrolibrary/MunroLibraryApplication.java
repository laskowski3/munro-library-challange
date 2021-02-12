package com.challange.munrolibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.challange")
@SpringBootApplication
public class MunroLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MunroLibraryApplication.class, args);
	}

}
