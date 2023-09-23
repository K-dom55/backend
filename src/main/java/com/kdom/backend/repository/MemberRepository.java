package com.kdom.backend.repository;

import com.kdom.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMacAddress(String ip);

    boolean existsByMacAddress(String ip);
}
