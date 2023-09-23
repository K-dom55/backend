package com.kdom.backend.repository;

import com.kdom.backend.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    boolean existsByArticle_IdAndMember_Ip(Long articleId, String ip);


    Integer countByArticleId(Long articleId);
}
