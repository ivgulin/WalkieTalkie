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

    public Profile edit(Profile profile) {
        if (profile.getPassword().equals(profile.getConfirmedPassword())) {
            profile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        }
        return profileRepository.save(profile);
    }

    public Profile findByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username);
        Set<Profile> friends = profileRepository.findFriends(username);
        Set<Profile> insideRequests = profileRepository.findInsideRequests(username);
        Set<Profile> outsideRequests = profileRepository.findOutsideRequests(username);

        if (friends.size() != 0) {
            profile.setFriends(friends);
        }
        if (insideRequests.size() != 0) {
            profile.setInsideFriendShipRequests(insideRequests);
        }
        if (outsideRequests.size() != 0) {
            profile.setOutsideFriendShipRequests(outsideRequests);
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

    public HashSet<Profile> findByFirstName(String firstName) {
        return profileRepository.findByFirstName(firstName);
    }

    public HashSet<Profile> findByLastName(String lastName) {
        return profileRepository.findByLastName(lastName);
    }

    public void sendFriendshipRequest(String username, String friendUsername) {
        Profile profile = profileRepository.findByUsername(username);
        Profile friendProfile = profileRepository.findByUsername(friendUsername);
        profile.setInsideFriendshipRequest(friendProfile);
        friendProfile.setOutsideFriendshipRequest(profile);
        profileRepository.save(profile);
        profileRepository.save(friendProfile);
    }

    public void acceptFriendship(String username, String friendUsername) {
        profileRepository.deleteInsideRequests(friendUsername, username);
        profileRepository.deleteOutsideRequests(username, friendUsername);
        Profile profile = profileRepository.findByUsername(username);
        profile.addFriend(profileRepository.findByUsername(friendUsername));
        profileRepository.save(profile);
    }

}
