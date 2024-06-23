package com.capstone.agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.agent.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findById(int id);
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
