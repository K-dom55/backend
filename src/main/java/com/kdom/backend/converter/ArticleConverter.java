package com.kdom.backend.converter;

import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Hashtag;
import com.kdom.backend.dto.response.ArticleResponseDto;

import java.util.List;

public class ArticleConverter {

    public static ArticleResponseDto.GetArticleDetail toArticleDto(Article article, List<String> keywords, Integer count){

        return ArticleResponseDto.GetArticleDetail.builder()
                .id(article.getId())
                .title(article.getTitle())
                .target(article.getTarget())
                .content(article.getContent())
                .imgUrl(article.getImgUrl())
                .linkUrl(article.getLinkUrl())
                .keywords(keywords)
                .likeCount(count)
                .build();
    }

    public static ArticleResponseDto.GetArticleDetailList toArticleDtoList(List<ArticleResponseDto.GetArticleDetail> articleResponseDtos){
        return ArticleResponseDto.GetArticleDetailList.builder()
                .getArticleDetails(articleResponseDtos)
                .build();

    }

    public static ArticleResponseDto.GetTargetDto toTargetDto(Article article,Integer count){

        return ArticleResponseDto.GetTargetDto.builder()
                .id(article.getId())
                .image_Url(article.getImgUrl())
                .target_name(article.getTarget())
                .Count(count)
                .build();
    }

    public static ArticleResponseDto.GetTargetDtoList toTargetDtoList(List<ArticleResponseDto.GetTargetDto> getTargetDtos){

        return ArticleResponseDto.GetTargetDtoList.builder()
                .getTargetDtos(getTargetDtos)
                .build();
    }



}
