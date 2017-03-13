package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    public Profile create(Profile profile) {
        return profileDAO.save(profile);
    }


    public Profile find(Long id) {
        return profileDAO.findOne(id);
    }
}
