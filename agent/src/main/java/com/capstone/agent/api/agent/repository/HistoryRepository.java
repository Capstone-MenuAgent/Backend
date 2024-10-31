package com.capstone.agent.api.agent.repository;
import com.capstone.agent.api.agent.entity.History;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findById(Long id);

    Optional<History> findByMemberId(Long memberId);
    List<History> findAllByMemberId(Long memberId);

    Optional<History> findByIdAndMemberId(Long id, Long memberId);
}
