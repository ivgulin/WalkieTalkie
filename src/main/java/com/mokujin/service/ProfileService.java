package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private ProfileDAO profileDAO;


    public Profile save(Profile profile) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        profile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        return profileDAO.save(profile);
    }


    public Profile find(Long id) {
        return profileDAO.findOne(id);
    }

    public Profile findByUsername(String username){
        return profileDAO.findByUserName(username);
    }

    public Profile findByEmail(String email){
        return profileDAO.findByEmail(email);
    }



}
