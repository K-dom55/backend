package com.kdom.backend.repository;

import com.kdom.backend.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    @Query()
    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable); //
    //List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
    List<Article> findTop10ByOrderByIdDesc();
    List<Article> findTop10ByTargetContainingOrderByIdDesc(String target);
    List<Article> findByTargetAndIdLessThanOrderByIdDesc(String target, Long id, Pageable pageable);


    @Query(value = "SELECT a.*, COALESCE(row_number() over (order by COALESCE(count(l.article_id), 0) desc), 0) as likeCountRank\n" +
            "FROM ARTICLE a\n" +
            "LEFT JOIN LIKES l ON l.article_id = a.article_id\n" +
            "WHERE a.article_id < :id\n" +
            "GROUP BY a.article_id, a.createdAt\n" +
            "ORDER BY a.createdAt DESC\n", nativeQuery = true)
    List<Object[]> findRankbyArticleId(@Param("id") Long articleId, Pageable pageable);

    @Query(value = "SELECT COALESCE(row_number() over (order by COALESCE(count(l.article_id), 0) desc), 0) as likeCountRank\n" +
            "FROM ARTICLE a\n" +
            "LEFT JOIN LIKES l ON l.article_id = a.article_id\n" +
            "WHERE a.article_id < 50\n" +
            "GROUP BY a.article_id, a.createdAt\n" +
            "ORDER BY a.createdAt DESC\n", nativeQuery = true)
    List<Integer> findRank();

    /*
    @Query(value = "SELECT article_id,\n" +
            "       createdAt,\n" +
            "       COALESCE(row_number() over (order by COALESCE(count(l.article_id), 0) desc), 0) as likeCountRank\n" +
            "FROM ARTICLE\n" +
            "LEFT JOIN LIKES l ON l.article_id = ARTICLE.article_id\n" +
            "WHERE article_id < 1000 and article_id = :your_id\n" +
            "ORDER BY createdAt DESC", nativeQuery = true)
    Integer findRankbyId(@Param("id") Long articleId);*/
}
