package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/add_profile", method = RequestMethod.POST)
    public String profile(@ModelAttribute("profile") Profile profile,
                          @ModelAttribute("file") MultipartFile file) {
        if (file != null) {
            profile.setPhoto(convertFileToByteArray(file));
        }
        profileService.create(profile);
        return "redirect:/";
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


}
