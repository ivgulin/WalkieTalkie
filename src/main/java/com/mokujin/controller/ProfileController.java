package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.ProfileValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileValidator validator;

    @Autowired
    private SecurityService securityService;

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

    @PostMapping("/registration")
    public Profile profile(@ModelAttribute("profile") Profile profile,
                           @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
        if (file != null) {
            profile.setPhoto(convertFileToByteArray(file));
        }
        validator.validate(profile, bindingResult);
        System.out.println(profile);//TODO clear this mess up after

        Profile savedProfile = profileService.save(profile);
        securityService.autologin(savedProfile.getUserName(), savedProfile.getPassword());
        return savedProfile;

    }


    @GetMapping("/login")
    public String login(Model model, String error, String logout){
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "index";
    }


    private byte[] convertFileToByteArray(MultipartFile file) {
        byte[] photo = null;
        try {
            photo = IOUtils.toByteArray(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }



    @GetMapping("/profile")
    public Profile profile(@RequestParam(value = "id", required = false, defaultValue = "1") Long id) {
        return profileService.find(id);
    }//TODO delete this after js set up

}
