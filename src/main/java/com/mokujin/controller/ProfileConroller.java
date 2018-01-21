package com.mokujin.controller;

import com.mokujin.domain.PasswordChanger;
import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.PasswordEditValidator;
import com.mokujin.validator.ProfileEditValidator;
import com.mokujin.validator.ProfileRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/profile")
public class ProfileConroller {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileEditValidator validator;

    @Autowired
    private SecurityService securityService;


    @GetMapping()
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        model.addAttribute("friends", profile.getFriends());
        return "profile";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        model.addAttribute("friends", profile.getFriends());
        return "edit";
    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute("profile") Profile profile, @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
        Profile editedProfile = setNewProfileProperties(profile);
        validator.validate(editedProfile, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }

        profileService.edit(editedProfile);
        return "redirect:/profile";

    }

    @GetMapping("/search")
    public String search(@RequestParam("find") String searchRequest, Model model) {
        Set<Profile> profiles = new HashSet<>();
        if (searchRequest.contains(" ")) {
            model.addAttribute("profiles", profileService.findByFullName(searchRequest));
            return "search";
        }
        Set<Profile> byFirstName = profileService.findByFirstName(searchRequest);
        Set<Profile> byLastName = profileService.findByLastName(searchRequest);
        Profile byUsername = profileService.findByUsername(searchRequest);

        if (byUsername != null) {
            profiles.add(byUsername);
        }
        if (!byFirstName.isEmpty()) {
            byFirstName.forEach(profiles::add);
        }
        if (!byLastName.isEmpty()) {
            byLastName.forEach(profiles::add);
        }
        model.addAttribute("profiles", profiles);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        model.addAttribute("friends", profile.getFriends());
        return "search";
    }


    @PostMapping("/add")
    public String addFriend(@RequestParam("username") String friendName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        profileService.sendFriendshipRequest(username, friendName);
        return "redirect:/profile";
    }

    @PostMapping("/accept")
    public String acceptFriendship(@RequestParam("username") String friendName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        profileService.acceptFriendship(username, friendName);
        return "redirect:/profile";
    }


    @GetMapping("/requests")
    public String requests(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("insideFriendshipRequests", profile.getInsideFriendShipRequests());
        model.addAttribute("outsideFriendShipRequests", profile.getOutsideFriendShipRequests());
        model.addAttribute("profile", profile);
        model.addAttribute("friends", profile.getFriends());
        return "requests";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("passwordChanger", new PasswordChanger());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        model.addAttribute("friends", profile.getFriends());
        return "editPassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("passwordChanger") PasswordChanger passwordChanger,
                                 Model model,
                                 BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());

        //if (passwordChanger.getOldPassword().equals(profile.getConfirmedPassword())) {
        if (true){
            if (passwordChanger.getNewPassword().equals(passwordChanger.getConfirmedPassword())) {
                profile.setPassword(passwordChanger.getNewPassword());
                profile.setConfirmedPassword(passwordChanger.getConfirmedPassword());
                PasswordEditValidator validator = new PasswordEditValidator();
                validator.validate(profile, bindingResult);
                if (bindingResult.getErrorCount() != 0) {
                    String message = "";
                    for (Object object : bindingResult.getAllErrors()) {
                        if (object instanceof ObjectError) {
                            ObjectError error = (ObjectError) object;
                            model.addAttribute("newPasswordError", message + "\n" + error.getDefaultMessage());
                            return "editPassword";
                        }
                    }
                } else {
                    profile = profileService.edit(profile);
                    securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
                    return "redirect:/profile";
                }

            } else {
                model.addAttribute("newPasswordError", "Passwords don't match.");
                return "editPassword";
            }
        } else {
            model.addAttribute("oldPasswordError", "Bad credential. Try again.");
            return "editPassword";
        }
        return "redirect:/profile";
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

        if (profile.getPhoto() != null) {
            savedProfile.setPhoto(profile.getPhoto());
        }
        return savedProfile;
    }


}
