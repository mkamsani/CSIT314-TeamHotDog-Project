package com.hotdog.ctbs.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Example {
    @GetMapping("/hello")
    public String getHello() {
        return "hello".toLowerCase();
    }
}