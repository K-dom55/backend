package com.kdom.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Schema(description = "Article 작성 Dto")
@NoArgsConstructor
public class ArticleRequestDto {

    @Schema(example = "문상훈을 보고나서 코로나가 완치됐어요!")
    public String title;


    @Schema(example = "문상훈")
    public String target;


    @Schema(example = "문상훈은 후암동에서 시작한 만능 개그맨으로, ...")
    public String content;


    @Schema(example = "https://youtube.com/...")
    public String linkUrl;

    @Schema(example = "[\"keyword1\",\"keyword2\", \"keyword3\"]")
    @Size(min = 1, max = 3)
    public List<String> keywords;

    public String imageUrl;

}
