package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public Profile save(Profile profile) {
        profile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        return profileDAO.save(profile);
    }

    public Profile findByUsername(String username) {
        Profile profile = profileDAO.findByUsername(username);
        profile.setFriends(profileDAO.findFriends(username));
        return profile;
    }

    public Profile findByEmail(String email) {
        return profileDAO.findByEmail(email);

    }

    public HashSet<Profile> findByFullName(String fullName) {
        String delimiter = " ";
            String firstName = fullName.substring(0, fullName.indexOf(delimiter));
            int firstIndexAfterDelimiter = (fullName.indexOf(delimiter));
            String lastName = fullName.substring(firstIndexAfterDelimiter++);
        return profileDAO.findByFullName(firstName, lastName);
    }

    public HashSet<Profile> findByFirstName(String firstName){
        return profileDAO.findByFirstName(firstName);
    }

    public HashSet<Profile> findByLastName(String lastName){
        return profileDAO.findByLastName(lastName);
    }

    public Profile addFriend(String username, String friend){
        Profile profile = profileDAO.findByUsername(username);
        profile.addFriend(profileDAO.findByUsername(friend));
        return profileDAO.save(profile);
    }

}
