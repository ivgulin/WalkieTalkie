package com.mokujin.repository;


import com.mokujin.domain.ChatMessageModel;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ChatMessageRepository extends GraphRepository<ChatMessageModel>{
}
