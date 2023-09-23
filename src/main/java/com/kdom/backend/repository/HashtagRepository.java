package com.kdom.backend.repository;

import com.kdom.backend.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByArticleId(Long articleId);
}
