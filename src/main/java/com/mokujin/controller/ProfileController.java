package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.ProfileValidator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller("/profile")
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

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Profile profile = profileService.findByUsername(username);
        if (profile == null || !password.equals(profile.getConfirmedPassword())) {
            return "redirect:/login?error";
        }
        securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
        return "redirect:/profile";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("profile") Profile profile,
                               @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
        if (file != null) {
            profile.setPhoto(convertMultiPartFileToByteArray(file));
        }
        validator.validate(profile, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        Profile savedProfile = profileService.save(profile);
        securityService.autologin(savedProfile.getUsername(), savedProfile.getConfirmedPassword());
        return "redirect:/profile";

    }


    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/profil")
    @ResponseBody
    public Profile profil(@RequestParam(value = "id", required = false, defaultValue = "1") Long id) {
        return profileService.find(id);
    }//TODO delete this after js set up


    private byte[] convertMultiPartFileToByteArray(MultipartFile file) {
        byte[] photo = null;
        try {
            photo = IOUtils.toByteArray(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }


}
