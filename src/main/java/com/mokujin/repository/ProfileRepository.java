package com.mokujin.repository;


import com.mokujin.domain.Profile;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public interface ProfileRepository extends GraphRepository<Profile> {

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.email = {email}\n" +
            "        RETURN profile")
    Profile findByEmail(@Param("email") String email);

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.username = {username}\n" +
            "        RETURN profile")
    Profile findByUsername(@Param("username") String username);

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.firstName = {firstName} AND profile.lastName={lastName}\n" +
            "        RETURN profile")
    HashSet<Profile> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.firstName = {firstName}\n" +
            "        RETURN profile")
    HashSet<Profile> findByFirstName(@Param("firstName") String firstName);

    @Query("MATCH (profile:Profile)\n" +
            "    WHERE profile.lastName = {lastName}\n" +
            "        RETURN profile")
    HashSet<Profile> findByLastName(@Param("lastName") String firstName);


    @Query("MATCH (profile:Profile)<-[:FRIENDS]->(friends)\n" +
            "WHERE profile.username = {username} RETURN  friends")
    Set<Profile> findFriends(@Param("username") String username);

    @Query("MATCH (profile:Profile)-[:OUTSIDE_FRIEND_SHIP_REQUESTS]->(users)\n" +
            "WHERE profile.username = {username} RETURN  users")
    Set<Profile> findOutsideRequests(@Param("username") String username);

    @Query("MATCH (profile:Profile)-[:INSIDE_FRIEND_SHIP_REQUESTS]->(users)\n" +
            "WHERE profile.username = {username} RETURN  users")
    Set<Profile> findInsideRequests(@Param("username") String username);

    @Query("MATCH (n:Profile)-[rel:INSIDE_FRIEND_SHIP_REQUESTS]->(r:Profile) \n"+
            "WHERE n.username={username} AND r.username={friendName} \n"+
            "DELETE rel")
    void deleteInsideRequests(@Param("username") String username, @Param("friendName") String friendName);

    @Query("MATCH (n:Profile)-[rel:OUTSIDE_FRIEND_SHIP_REQUESTS]->(r:Profile) \n"+
            "WHERE n.username={username} AND r.username={friendName} \n"+
            "DELETE rel")
    void deleteOutsideRequests(@Param("username") String username, @Param("friendName") String friendName);
}
