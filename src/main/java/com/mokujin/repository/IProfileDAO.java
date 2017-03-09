package com.mokujin.repository;


import com.mokujin.domain.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IProfileDAO implements ProfileDAO {

    List<Profile> profiles = new ArrayList<>();


    @Override
    public void add(Profile profile) {
        profiles.add(profile);
        profiles.forEach(System.out::println);
    }
}