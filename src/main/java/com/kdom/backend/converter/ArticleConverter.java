package com.kdom.backend.converter;

import com.kdom.backend.domain.Article;
import com.kdom.backend.dto.response.ArticleDetailResponseDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.dto.response.ArticleTargetListResponseDto;

import java.util.List;

public class ArticleConverter {

    //사용안함
   /* public static ArticleResponseDto.GetArticleDetail toArticleDto(Article article, List<String> keywords, Integer count){
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
    }*/

    public static ArticleDetailResponseDto toArticleDto(Article article, List<String> keywords, Integer count){

        return ArticleDetailResponseDto.builder()
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


    public static ArticleTargetListResponseDto toTargetDto(Article article, Integer count){

        return ArticleTargetListResponseDto.builder()
                .id(article.getId())
                .image_Url(article.getImgUrl())
                .target_name(article.getTarget())
                .count(count)
                .build();
    }

    /*public static ArticleResponseDto.GetTargetDtoList toTargetDtoList(List<ArticleResponseDto.GetTargetDto> getTargetDtos){
        return ArticleResponseDto.GetTargetDtoList.builder()
                .getTargetDtos(getTargetDtos)
                .build();
    }*/
}
