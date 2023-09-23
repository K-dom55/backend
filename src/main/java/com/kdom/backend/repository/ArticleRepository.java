package com.kdom.backend.repository;

import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
}
