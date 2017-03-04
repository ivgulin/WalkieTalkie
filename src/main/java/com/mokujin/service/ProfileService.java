package com.mokujin.service;

import com.mokujin.domain.Profile;
import com.mokujin.repository.ProfileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProfileService {

    @Autowired
    private ProfileDAO profileDAO;



    public void create(Profile profile) {
         profileDAO.save(profile);
    }
}
