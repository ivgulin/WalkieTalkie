package com.mokujin.repository;


import com.mokujin.domain.Profile;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ProfileDAO extends GraphRepository<Profile> {


}