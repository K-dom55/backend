package com.kdom.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ArticleDetailResponseDto {
    private Long id;

    private String target;

    private String title;

    private String content;

    private String imgUrl;

    private String linkUrl;

    private List<String> keywords;

    private Integer likeCount;

<<<<<<< HEAD
    private Integer linkRank;
=======
    private LocalDateTime createAt;
>>>>>>> c510e5455e0c83ef5f8e3720bb13cb24ef9454b3
}
