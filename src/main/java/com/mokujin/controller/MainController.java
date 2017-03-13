package com.mokujin.controller;

import com.mokujin.domain.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping
    public String home(ModelAndView modelAndView) {
        modelAndView.addObject("profile", new Profile());
        return "index";
    }
}
