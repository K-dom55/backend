package com.kdom.backend.service;

import com.kdom.backend.dto.response.ArticleDetailResponseDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.dto.response.ArticleTargetListResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    /**
     * 파일 업로드
     * @Input MultipartFile 파일
     * @Output String 파일 url
     * ~한 함수
     * */

    String uploadImage(MultipartFile multipartFile) throws IOException;

    /**
     * 글 작성
     *
     * @return
     * @Input 글 작성에 필요한 내용들
     * @Output String 글 작성 완료 여부
     * ~한 함수
     */
    ArticleResponseDto uploadArticle(String title, String content, String imageUrl, String linkUrl, List<String> keyword, String target);
    //String uploadArticle(String title, String content, String imageUrl, String linkUrl, List<String> keyword, String target);

    ArticleDetailResponseDto findArticleDetail(Long articleId);

    List<ArticleDetailResponseDto> findArticleList(Long articleId);

    List<ArticleDetailResponseDto> findArticleFirstList();

    List<ArticleDetailResponseDto> findArticleListByTarget(Long articleId, String target_name);
    List<ArticleDetailResponseDto> findArticleFirstListByTarget(String target_name);
    List<ArticleDetailResponseDto> findArticleRankList(Long article_id);


    //ArticleResponseDto.GetTargetDtoList findArticleTargetRankList(Long article_id);
    List<ArticleTargetListResponseDto> findArticleTargetRankList(Long article_id);
}


