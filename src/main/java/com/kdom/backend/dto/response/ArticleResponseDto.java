package com.kdom.backend.dto.response;

import com.kdom.backend.domain.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ArticleResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GetArticleDetail {

        private Long id;

        private String title;

        private String content;

        private String imgUrl;

        private String linkUrl;

        private List<String> keywords;

        private Integer likeCount;


    }




}
