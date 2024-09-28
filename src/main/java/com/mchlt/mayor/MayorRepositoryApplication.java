package com.mchlt.mayor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.mchlt.mayor.repository")
public class MayorRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MayorRepositoryApplication.class, args);
    }

}
