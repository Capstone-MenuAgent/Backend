package com.capstone.agent.api.member.repository;

import com.capstone.agent.api.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    
    Optional<Member> findByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);
    
    boolean existsByEmail(String email);
}