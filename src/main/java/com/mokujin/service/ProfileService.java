package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileRepository;
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
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public Profile save(Profile profile) {
        profile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        return profileRepository.save(profile);
    }

    public Profile findByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username);
        Set<Profile> friends = profileRepository.findFriends(username);
        if (friends.size()!=0) {
            profile.setFriends(friends);
        }
        return profile;
    }

    public Profile findByEmail(String email) {
        return profileRepository.findByEmail(email);

    }

    public HashSet<Profile> findByFullName(String fullName) {
        String delimiter = " ";
            String firstName = fullName.substring(0, fullName.indexOf(delimiter));
            int firstIndexAfterDelimiter = (fullName.indexOf(delimiter));
            String lastName = fullName.substring(firstIndexAfterDelimiter++);
        return profileRepository.findByFullName(firstName, lastName);
    }

    public HashSet<Profile> findByFirstName(String firstName){
        return profileRepository.findByFirstName(firstName);
    }

    public HashSet<Profile> findByLastName(String lastName){
        return profileRepository.findByLastName(lastName);
    }

    public Profile addFriend(String username, String friend){
        Profile profile = profileRepository.findByUsername(username);
        profile.addFriend(profileRepository.findByUsername(friend));
        return profileRepository.save(profile);
    }

}
