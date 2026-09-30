package com.poly.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.poly")
@EnableJpaRepositories(basePackages = "com.poly.dao")
@EntityScan(basePackages = "com.poly.entity")
public class ShopThoiTrangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopThoiTrangApplication.class, args);
    }
}