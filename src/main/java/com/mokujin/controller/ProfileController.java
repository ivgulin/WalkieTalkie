package com.mokujin.controller;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @PostMapping("/add_profile")
    public Profile profile(@ModelAttribute("profile") Profile profile,
                           @RequestParam("file") MultipartFile file) {
        if (file != null) {
            profile.setPhoto(convertFileToByteArray(file));
        }
        System.out.println(profile);
        return profileService.save(profile);
    }

    @GetMapping("/profile")
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
