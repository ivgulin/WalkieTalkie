package com.mokujin;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;


@EnableNeo4jRepositories
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("application-context.xml");
        ProfileService profileService = (ProfileService) applicationContext.getBean("profileService");
        Profile profile =
                new Profile("new","name","surname","mail","password");
        profileService.create(profile);

    }


}
