package com.capstone.agent.legacy.repository;
import com.capstone.agent.legacy.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findById(int id);
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
