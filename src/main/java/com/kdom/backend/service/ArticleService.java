package com.kdom.backend.service;

import com.kdom.backend.dto.response.ArticleResponseDto;
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
     * @Input 글 작성에 필요한 내용들
     * @Output String 글 작성 완료 여부
     * ~한 함수
     * */
    String uploadArticle(String title, String content, String imageUrl, String linkUrl, List<String> keyword,String target);

    ArticleResponseDto.GetArticleDetail findArticleDetail(Long articleId);

    ArticleResponseDto.GetArticleDetailList findArticleList(Long articleId);

    ArticleResponseDto.GetArticleDetailList findArticleFirstList();

    ArticleResponseDto.GetArticleDetailList findArticleListByTarget(Long articleId, String target_name);

    ArticleResponseDto.GetArticleDetailList findArticleRankList(Long article_id);

    ArticleResponseDto.GetTargetDtoList findArticleTargetRankList(Long article_id);
}


