package com.students2.student2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Student2Application {

	public static void main(String[] args) {
		SpringApplication.run(Student2Application.class, args);
	}

}
