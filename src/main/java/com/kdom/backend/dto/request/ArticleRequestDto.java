package com.kdom.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ArticleRequestDto {

    @Schema(description = "Article 작성 Dto")
    @Getter
    @Setter
    public static class postArticleDto{

        @Schema(example = "title_example")
        public String title;

        @Schema(example = "content_example")
        public String content;

        @Schema(example = "imageUrl_example")
        public String imageUrl;

        @Schema(example = "linkUrl_example")
        public String linkUrl;

        @Schema(example = "")
        @Size(min = 0, max = 3)
        public List<String> keyword1;



    }
}
