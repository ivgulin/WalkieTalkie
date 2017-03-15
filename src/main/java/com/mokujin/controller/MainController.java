package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.util.MailUtil;
import com.mokujin.validator.ProfileValidator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileValidator validator;

    @Autowired
    private SecurityService securityService;


    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        if (isUserAuthenticated()) return "redirect:/profile";

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
        if (isUserAuthenticated()) return "redirect:/profile";
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

        profile = profileService.save(profile);
        securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
        //will be commented until end of development
        //MailUtil.sendAfterRegistrationMail(profile.getEmail());
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        return "profile";
    }


    private Profile setNewProfileProperties(Profile profile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile savedProfile = profileService.findByUsername(authentication.getName());
        if (profile.getUsername() != null) {
            savedProfile.setUsername(profile.getUsername());
        }
        if (profile.getFirstName() != null) {
            savedProfile.setFirstName(profile.getFirstName());
        }
        if (profile.getLastName() != null) {
            savedProfile.setLastName(profile.getLastName());
        }
        if (profile.getEmail() != null) {
            savedProfile.setEmail(profile.getEmail());
        }
        if (profile.getPassword() != null) {
            savedProfile.setUsername(profile.getPassword());
        }
        if (profile.getPhoto() != null) {
            savedProfile.setPhoto(profile.getPhoto());
        }
        return savedProfile;
    }

    private byte[] convertMultiPartFileToByteArray(MultipartFile file) {
        byte[] photo = null;
        try {
            photo = IOUtils.toByteArray(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(authentication.getName(), "anonymousUser");
    }

}
