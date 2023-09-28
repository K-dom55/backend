package com.kdom.backend.converter;

import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Hashtag;
import com.kdom.backend.dto.request.ArticleRequestDto;
import com.kdom.backend.dto.response.ArticleDetailResponseDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.dto.response.ArticleTargetListResponseDto;

import java.util.List;

public class ArticleConverter {

    //Article.builder().title(title).content(content).imgUrl(imageUrl).linkUrl(linkUrl).target(target).build();
    public static Article toArticle(ArticleRequestDto dto){
        return Article.builder()
                .title(dto.title)
                .content(dto.content)
                .imgUrl(dto.imageUrl)
                .linkUrl(dto.linkUrl)
                .target(dto.target)
                .build();
    }

    public static ArticleResponseDto toArticleDto(Article article, List<String> keywords) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .target(article.getTarget())
                .content(article.getContent())
                .imgUrl(article.getImgUrl())
                .linkUrl(article.getLinkUrl())
                .keywords(keywords)
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleDetailResponseDto toArticleDetailDto(Article article, List<String> keywords, Integer count, Integer rank){

        return ArticleDetailResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .target(article.getTarget())
                .content(article.getContent())
                .imgUrl(article.getImgUrl())
                .linkUrl(article.getLinkUrl())
                .keywords(keywords)
                .likeCount(count)
                .linkRank(rank)
                .createAt(article.getCreatedAt())
                .build();
    }

/*    public static ArticleResponseDto.GetArticleDetailList toArticleDtoList(List<ArticleResponseDto.GetArticleDetail> articleResponseDtos){
        return ArticleResponseDto.GetArticleDetailList.builder()
                .getArticleDetails(articleResponseDtos)
                .build();
    }*/


    public static ArticleTargetListResponseDto toTargetDto(Article article, Integer count){

        return ArticleTargetListResponseDto.builder()
                .id(article.getId())
                .image_Url(article.getImgUrl())
                .target_name(article.getTarget())
                .count(count)
                .createAt(article.getCreatedAt())
                .build();
    }

    /*public static ArticleResponseDto.GetTargetDtoList toTargetDtoList(List<ArticleResponseDto.GetTargetDto> getTargetDtos){
        return ArticleResponseDto.GetTargetDtoList.builder()
                .getTargetDtos(getTargetDtos)
                .build();
    }*/
}
