package com.todotogether.todo_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoBackendApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.load();
//
//		// 환경변수를 시스템 속성으로 등록 (Spring이 ${}로 인식할 수 있게)
//		System.setProperty("DB_URL", dotenv.get("DB_URL"));
//		System.setProperty("DB_USER", dotenv.get("DB_USER"));
//		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

		SpringApplication.run(TodoBackendApplication.class, args);
	}

}
