package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.ProfileRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.mokujin.util.MultiPartFileUtil.convertMultiPartFileToByteArray;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRegistrationValidator validator;

    @Autowired
    private SecurityService securityService;


    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (isUserAuthenticated()) return "redirect:/profile";
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
        if (isUserAuthenticated()) return "redirect:/profile";
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        model.addAttribute("file", null);
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }



    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(authentication.getName(), "anonymousUser");
    }

}
