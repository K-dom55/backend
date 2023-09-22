package com.kdom.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ArticleRequestDto {

    @Schema(description = "Article 작성 Dto")
    @Getter
    @Setter
    public static class postArticleDto{

        @NotNull
        @Schema(example = "title_example")
        public String title;

        @NotNull
        @Schema(example = "content_example")
        public String content;

        //@NotNull
        //@Schema(example = "imageUrl_example")
        //public String imageUrl;

        public MultipartFile multipartFile;

        @NotNull
        @Schema(example = "linkUrl_example")
        public String linkUrl;

        @NotNull
        @Schema(example = "")
        @Size(min = 0, max = 3)
        public List<String> keyword1;



    }
}