package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.IProfileService;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {

    @Autowired
    ProfileService profileService;


    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/add_profile", method = RequestMethod.POST)
    public String profile(@ModelAttribute("profile")Profile profile){
        profileService.create(profile);
        return "redirect:/";
    }


}
