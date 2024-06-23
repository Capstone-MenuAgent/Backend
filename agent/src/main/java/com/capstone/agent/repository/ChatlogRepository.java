package com.capstone.agent.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.agent.entity.Chatlog;

@Repository
public interface ChatlogRepository extends JpaRepository<Chatlog, Integer>{
    List<Chatlog> findByUserId(int userId);
}
