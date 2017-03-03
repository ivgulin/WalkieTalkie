package com.mokujin.bean;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "profileBean")
public class ProfileBean {
    Profile profile = new Profile();

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void create(){
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("application-context.xml");
        ProfileService profileService = (ProfileService) applicationContext.getBean("profileService");

        profileService.create(profile);

        applicationContext.close();
    }
}
