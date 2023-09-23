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
<<<<<<< Updated upstream
}exit


=======

    @Query("select a from Article a where a.id<:id order by a.id desc ")
    List<Article> findArticleByIdLessThanOrderByIdDesc(@Param("id") Long id, Pageable pageable);

    List<Article> findByIdGreaterThanOrderByIdDesc(Long id, Pageable pageable);


    //List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable, Sort sort);
    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
}
>>>>>>> Stashed changes
