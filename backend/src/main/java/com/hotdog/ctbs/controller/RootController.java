package com.hotdog.ctbs.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class RootController {

    // Return the file index.html from the static folder
    @GetMapping(value = "")
    public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes)
    {
        return new RedirectView("http://localhost:8000/api/index.html");
    }
}