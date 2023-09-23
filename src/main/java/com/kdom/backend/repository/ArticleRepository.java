package com.kdom.backend.repository;

import com.kdom.backend.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    //jpql은 자기 하위 아래 order by 못함 ex. order by(select * ~) 못함
    @Query("SELECT DISTINCT a, COUNT(l.id) AS likeCount\n" +
            "FROM Article a\n" +
            "LEFT JOIN Likes l ON l.article.id = a.id\n" +
            "WHERE a.id > :id\n" +
            "GROUP BY a.id, a.title, a.content, a.imgUrl, a.linkUrl, a.target\n" +
            "ORDER BY likeCount DESC")
    List<Article> findAllByLikeRank(@Param("id") Long article_id, Pageable pageable);

    @Query("SELECT a, COUNT(a.target) AS target_count FROM Article a WHERE a.id > :id GROUP BY a.target ORDER BY target_count DESC")
    List<Article> findAllByNameRank(@Param("id")Long article_id,Pageable pageable);

    Integer countByTarget(String target);

    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
    List<Article> findTop10ByOrderByIdDesc();
    List<Article> findTop10ByTargetOrderByIdDesc(String target);
    List<Article> findByTargetAndIdLessThanOrderByIdDesc(String target, Long id, Pageable pageable);

}
