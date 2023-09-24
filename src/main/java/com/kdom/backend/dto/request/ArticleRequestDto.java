package com.kdom.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
public class ArticleRequestDto {

    @Schema(description = "Article 작성 Dto")
    @Getter
    @Setter
    public static class postArticleDto{


        @Schema(example = "문상훈을 보고나서 코로나가 완치됐어요!")
        public String title;


        @Schema(example = "문상훈")
        public String target;


        @Schema(example = "문상훈은 후암동에서 시작한 만능 개그맨으로, ...")
        public String content;


        @Schema(example = "https://youtube.com/...")
        public String linkUrl;


        @Schema(example = "[#귀여움, # 듬직함, ...]")
        @Size(min = 1, max = 3)
        public List<String> keyword;

        public String imageUrl;
    }
}
