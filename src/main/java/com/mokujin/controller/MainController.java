package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.ProfileRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MainController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRegistrationValidator validator;

    @Autowired
    private SecurityService securityService;


    @GetMapping
    public String home() {
        log.info("'home() invoked with no param'");
        String redirect = "index";
        log.info("'home() redirects on {}.jsp'", redirect);
        return redirect;
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        log.info("'login() invoked with params - {}, {} , {}'", model, error, logout);
        if (isUserAuthenticated()) {
            String redirect = "redirect:/profile";
            log.info("'login(); user authenticated - {}'", redirect);
            return redirect;
        }
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        String redirect = "index";
        log.info("'login() returned model - {}, redirected to {}.jsp'", model, redirect);
        return redirect;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("'login() invoked with params - {}, {}'", username, password);
        Profile profile = profileService.findByUsername(username);
        log.info("'profile = {}'", profile);
        if (profile == null || !password.equals(profile.getConfirmedPassword())) {
            String redirect = "redirect:/login?error";
            log.info("'login redirected to {}'", redirect);
            return redirect;
        }
        securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
        String redirect = "redirect:/profile";
        log.info("'login redirected to {}'", redirect);
        return redirect;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        log.info("'registration() invoked with param - {}'", model);
        if (isUserAuthenticated()) {
            String redirect = "redirect:/profile";
            log.info("'registration() redirected to {}'", redirect);
            return redirect;
        }
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        model.addAttribute("file", null);
        String redirect = "registration";
        log.info("'registration() returned model - {}, redirected to {}.jsp'", model, redirect);
        return redirect;
    }


    @PostMapping("/registration")
    public String registration(@ModelAttribute("profile") Profile profile,
                               @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
        log.info("'registration() invoked with params - {}, {}, {}'", profile, file, bindingResult);
        if (file != null) {
            profile.setPhoto(convertMultiPartFileToByteArray(file));
        }
        validator.validate(profile, bindingResult);

        if (bindingResult.hasErrors()) {
            String redirect = "registration";
            log.info("'registration() redirected to {}.jsp'", redirect);
            return redirect;
        }

        profile = profileService.save(profile);
        log.info("'profile saved to database - {}'", profile);
        securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
        //will be commented until end of development
        //MailUtil.sendAfterRegistrationMail(profile.getEmail());
        String redirected = "redirect:/profile";
        log.info("'registration() redirected to {}'", redirected);
        return redirected;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("'logout() invoked with params {}, {}'", request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        String redirect = "redirect:/login?logout";
        log.info("'logout() redirected to {}'", redirect);
        return redirect;
    }


    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(authentication.getName(), "anonymousUser");
    }

}
