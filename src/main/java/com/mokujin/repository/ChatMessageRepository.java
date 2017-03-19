package com.mokujin.repository;


import com.mokujin.domain.ChatMessageModel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends GraphRepository<ChatMessageModel> {

    @Query("MATCH (message:ChatMessageModel)\n" +
            " WHERE (message.author = {username} AND message.receiver = {friendName})\n" +
            " OR (message.author = {friendName} AND message.receiver={username})\n " +
            "RETURN message ORDER BY message.createDate DESC LIMIT 5 ")
    public List<ChatMessageModel> getDialogue(@Param("username") String sender, @Param("friendName") String receiver);
}
