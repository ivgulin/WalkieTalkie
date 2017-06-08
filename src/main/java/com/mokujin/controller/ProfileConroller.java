package com.mokujin.controller;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.service.SecurityService;
import com.mokujin.validator.ProfileEditValidator;
import com.mokujin.validator.ProfileRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("insideFriendshipRequests", profile.getInsideFriendShipRequests());
        model.addAttribute("outsideFriendShipRequests", profile.getOutsideFriendShipRequests());
        return "profile";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.findByUsername(authentication.getName());
        model.addAttribute("profile", profile);
        return "edit";
    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute("profile") Profile profile, @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
        Profile editedProfile = setNewProfileProperties(profile);
        validator.validate(editedProfile, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }

        profile = profileService.edit(editedProfile);
        securityService.autologin(profile.getUsername(), profile.getConfirmedPassword());
        //will be commented until end of development
        //MailUtil.sendAfterRegistrationMail(profile.getEmail());
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
      /*  if (profile.getPassword() != null && profile.getConfirmedPassword() != null) {
            savedProfile.setPassword(profile.getConfirmedPassword());
            savedProfile.setConfirmedPassword(profile.getConfirmedPassword());
        }*/
        if (profile.getPhoto() != null) {
            savedProfile.setPhoto(profile.getPhoto());
        }
        return savedProfile;
    }


}
