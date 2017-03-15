package com.mokujin.controller;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/{username}")
    public Profile profile(@PathVariable("username") String username) {
        return profileService.findByUsername(username);
    }
}
