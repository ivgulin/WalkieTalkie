package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("/")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @PostMapping("/add_profile")
    public Profile profile(@ModelAttribute("profile") Profile profile,
                           @ModelAttribute("file") MultipartFile file) {
        if (file != null) {
            profile.setPhoto(convertFileToByteArray(file));
        }
        return profileService.create(profile);
    }

    @RequestMapping(value = "/profile")
    public Profile profile(@RequestParam(value = "id", required = false, defaultValue = "1") Long id) {
        return profileService.find(id);
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
