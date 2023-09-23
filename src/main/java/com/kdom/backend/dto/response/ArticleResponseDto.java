package com.kdom.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ArticleResponseDto {

        private Long id;

        private String target;

        private String title;

        private String content;

        private String imgUrl;

        private String linkUrl;

        private List<String> keywords;

        private LocalDateTime createdAt;
}
