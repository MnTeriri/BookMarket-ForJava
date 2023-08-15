package com.example.bookmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class BookMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookMarketApplication.class, args);
    }

}
