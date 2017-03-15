package com.mokujin.repository;


import com.mokujin.domain.Profile;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;


public interface ProfileDAO extends GraphRepository<Profile> {

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.email = {email}\n" +
            "        RETURN profile")
    Profile findByEmail(@Param("email") String email);

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.username = {username}\n" +
            "        RETURN profile")
    Profile findByUsername(@Param("username") String username);
}
