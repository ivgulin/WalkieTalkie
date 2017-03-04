package com.mokujin;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by mokujin on 04.03.2017.
 */
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        ProfileService profileService = (ProfileService) applicationContext.getBean("profileService");
        Profile profile = new Profile();
        profile.setUserName("user");
        profile.setFirstName("first");
        profile.setLastName("last");
        profile.setEmail("email");
        profile.setPassword("password");

        profileService.create(profile);
        System.out.println("Profile Created successfully");
    }
}
