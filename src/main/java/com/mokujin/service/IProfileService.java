package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("profileService")
public class IProfileService implements ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    public Profile create(Profile profile) {
        return profileDAO.save(profile);
    }
}
