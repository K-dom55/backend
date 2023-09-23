package com.kdom.backend.repository;

import com.kdom.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< Updated upstream
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByIp(String ip);

    boolean existsByIp(String ip);
=======
public interface MemberRepository extends JpaRepository<Member, Long>{
>>>>>>> Stashed changes
}
