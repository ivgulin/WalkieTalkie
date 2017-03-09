package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.IProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProfileService {

    private final IProfileDAO profileDAO;

    @Autowired
    public ProfileService(IProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }


    public void create(Profile profile) {
        profileDAO.add(profile);
    }
}
