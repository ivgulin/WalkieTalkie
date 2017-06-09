package com.mokujin.controller;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequestMapping("/img")
public class ImageController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/photo")
    public void showPhoto(@RequestParam("username") String username, HttpServletResponse response)
            throws ServletException, IOException {
        Profile profile = profileService.findByUsername(username);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        byte[] photo = profile.getPhoto();
        if (photo.length == 0) {
            photo = getDefaultPhoto();
        }
        response.getOutputStream().write(photo);
        response.getOutputStream().close();
    }

    @GetMapping("/logo")
    public void showLogo(HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        File defaultImage = new File(System.getProperty("user.dir"),"src/main/resources/public/image/header-logo.jpg");
        byte[] logo = Files.readAllBytes(defaultImage.toPath());
        response.getOutputStream().write(logo);
        response.getOutputStream().close();
    }

    @GetMapping("/fullLogo")
    public void showFullLogo(HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        File defaultImage = new File(System.getProperty("user.dir"),"src/main/resources/public/image/logo.png");
        byte[] logo = Files.readAllBytes(defaultImage.toPath());
        response.getOutputStream().write(logo);
        response.getOutputStream().close();
    }

    private byte[] getDefaultPhoto() throws IOException {
        File defaultImage = new File(System.getProperty("user.dir"),"src/main/resources/public/image/default.jpg");
        return Files.readAllBytes(defaultImage.toPath());
    }


}
