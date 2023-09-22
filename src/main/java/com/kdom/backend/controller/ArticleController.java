package com.kdom.backend.controller;

import com.kdom.backend.config.BaseResponse;
import com.kdom.backend.config.Code;
import com.kdom.backend.dto.request.ArticleRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {


    /**
     * 글 작성 API
     * [POST] /article
     * @return BaseResponse<String>
     * */

    @Operation(summary = "글 작성 API")
    @PostMapping("/")
    public BaseResponse<String> PostArticle(@Validated  ArticleRequestDto.postArticleDto request){



        return new BaseResponse<>(Code.SUCCESS);
    }


}
