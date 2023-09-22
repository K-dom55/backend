package com.kdom.backend.repository;

import com.kdom.backend.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
