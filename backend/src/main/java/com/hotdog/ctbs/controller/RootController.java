package com.hotdog.ctbs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class RootController {

    /**
     * @return backend/src/main/resources/static/index.html
     */
    @GetMapping(value = "")
    public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes)
    {
        return new RedirectView("http://localhost:8000/api/index.html");
    }
}