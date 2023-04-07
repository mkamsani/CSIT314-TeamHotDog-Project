package com.hotdog.ctbs.controller;

//import com.example.backendapplication.biz.impl.WaypointServiceImpl;
//import com.example.backendapplication.enumeration.DeliveryStatus;
//import com.example.backendapplication.model.Waypoint;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class Example {
    @GetMapping("/hello")
    public String getHello() {
        return "hello".toLowerCase();
    }
}