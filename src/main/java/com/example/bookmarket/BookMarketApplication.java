package com.example.bookmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bookmarket.dao")
public class BookMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookMarketApplication.class, args);
    }

}
