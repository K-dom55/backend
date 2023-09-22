package com.kdom.backend.controller;

import com.kdom.backend.config.BaseResponse;
import com.kdom.backend.config.Code;
import com.kdom.backend.dto.request.ArticleRequestDto;
import com.kdom.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

import static com.kdom.backend.config.Code.ERRARTICLEREPO;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 글 작성 API
     * [POST] /article
     * @return BaseResponse<String>
     * */

    @Operation(summary = "글 작성 API", description = "multi file과 dto를 swagger에서 동시에 보낼 수 없어 swagger를 참고로 postman을 사용해주길 바랍니다. ")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 이거 cunsumes설정 이렇게 해줘야 swagger에서 파일 직접 선택 할 수 있다 ㅜㅜ
    public BaseResponse<String> PostArticle(@Validated @RequestPart("dto") ArticleRequestDto.postArticleDto request , @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @Validated @RequestPart("file") MultipartFile multiPartFile){
        try {
            String S3Url = articleService.uploadImage(multiPartFile);
            if(S3Url==null || S3Url.isEmpty() || S3Url.isBlank()){
                return new BaseResponse<>(Code.EMPTYS3URL);
            }
            String success = articleService.uploadArticle(request.getTitle(), request.getContent(), S3Url, request.getLinkUrl(), request.getKeyword());
            if(success==null){
                return new BaseResponse<>(Code.ERRARTICLEREPO);
            }
            return new BaseResponse<>(Code.SUCCESS);
        }catch (IOException i){
            System.out.println(i.getMessage());
            return new BaseResponse<>(Code.IOEXCEPTION);
        }
    }

    public BaseResponse<String> GetArticle(@Validated @RequestParam Long articleId){



        return null;g

    }


}
