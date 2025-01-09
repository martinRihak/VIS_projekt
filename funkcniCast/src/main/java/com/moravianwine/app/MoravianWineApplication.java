package com.moravianwine.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.moravianwine.app.repository")
public class MoravianWineApplication {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(MoravianWineApplication.class, args);
	}

}
