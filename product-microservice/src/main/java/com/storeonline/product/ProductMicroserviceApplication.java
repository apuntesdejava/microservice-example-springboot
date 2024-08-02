package com.storeonline.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ProductMicroserviceApplication {
 

    public static void main(String[] args) {
        SpringApplication.run(ProductMicroserviceApplication.class, args);

    }

}
