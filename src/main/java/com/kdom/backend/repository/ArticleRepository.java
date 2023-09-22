package com.kdom.backend.repository;

import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
