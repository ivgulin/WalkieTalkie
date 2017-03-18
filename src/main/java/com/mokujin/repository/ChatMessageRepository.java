package com.mokujin.repository;


import com.mokujin.domain.ChatMessageModel;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends GraphRepository<ChatMessageModel>{
}
