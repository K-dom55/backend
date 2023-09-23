package com.kdom.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ArticleTargetListResponseDto {
    private Long id;
    private String image_Url;
    private String target_name;
    private Integer count;
}
