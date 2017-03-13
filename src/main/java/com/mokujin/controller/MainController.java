package com.mokujin.controller;

import com.mokujin.domain.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        model.addAttribute("file", null);
        return "registration";
    }
}
