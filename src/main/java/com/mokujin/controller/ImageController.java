package com.mokujin.controller;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Controller
@RequestMapping("/img")
public class ImageController {
    @Autowired
    private ProfileService profileService;

    @GetMapping(value = "/photo")
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

    private byte[] getDefaultPhoto() throws IOException {
        File defaultImage = new File(System.getProperty("user.dir"),"src/main/resources/public/image/default.jpg");
        return Files.readAllBytes(defaultImage.toPath());
    }


}
