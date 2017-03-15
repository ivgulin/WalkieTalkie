package com.mokujin.controller;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/img")
public class ImageController {
    @Autowired
    private ProfileService profileService;

    @GetMapping(value = "/photo")
    public void showPhoto(@RequestParam("username") String username, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {
        Profile profile = profileService.findByUsername(username);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(profile.getPhoto());
        response.getOutputStream().close();
    }
}
