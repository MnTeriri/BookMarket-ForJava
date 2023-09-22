package com.example.bookmarketpassport.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TestController")
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "sdasdasd";
    }

    @RequestMapping("/test1")
    @PreAuthorize("hasAuthority('admin:user')")
    public String test1() {
        return "53435646534";
    }

    @RequestMapping("/test2")
    @PreAuthorize("hasAuthority('admin:user')")
    public String test2() {
        return "654656569";
    }
}
