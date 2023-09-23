package com.kdom.backend.dto.response;

import com.kdom.backend.domain.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ArticleResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GetArticleDetail {

        private Long id;

        private String target;

        private String title;

        private String content;

        private String imgUrl;

        private String linkUrl;

        private List<String> keywords;

        private Integer likeCount;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetArticleDetailList{

        private List<GetArticleDetail> getArticleDetails;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GetTargetDto{

        private Long id;
        private String image_Url;

        private String target_name;

        private Integer Count;


    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GetTargetDtoList{

        private List<GetTargetDto> getTargetDtos;
    }



}
