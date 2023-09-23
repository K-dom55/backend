package com.kdom.backend.controller;

import com.kdom.backend.converter.ArticleConverter;
import com.kdom.backend.dto.request.ArticleRequestDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.exception.BaseResponse;
import com.kdom.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static com.kdom.backend.exception.ExceptionCode.*;

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
    public BaseResponse<String> PostArticle(@Validated @RequestPart("dto") @Parameter() ArticleRequestDto.postArticleDto request , @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @Validated @RequestPart("file") MultipartFile multiPartFile){
        try {
            String S3Url = articleService.uploadImage(multiPartFile);
            if(S3Url==null || S3Url.isEmpty() || S3Url.isBlank()){
                return new BaseResponse<>(EMPTYS3URL);
            }
            String success = articleService.uploadArticle(request.getTitle(), request.getContent(), S3Url, request.getLinkUrl(), request.getKeyword());
            if(success==null){
                return new BaseResponse<>(ERRARTICLEREPO);
            }
            return new BaseResponse<>(SUCCESS);
        }catch (IOException i){
            System.out.println(i.getMessage());
            return new BaseResponse<>(IOEXCEPTION);
        }
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회를 위한 API입니다.")
    @GetMapping("/")
    public BaseResponse<ArticleResponseDto.GetArticleDetail> GetArticle(@Parameter(description = "article id를 입력해주세요") @Validated @RequestParam Long articleId){
        try {

            ArticleResponseDto.GetArticleDetail getArticleDetail = articleService.findArticleDetail(articleId);

            if (getArticleDetail == null) {

                return new BaseResponse<>(EMPTYARTICLEDTO);
            }

            return new BaseResponse<>(getArticleDetail);

        }catch(Exception e){
            System.out.println(e);
            return new BaseResponse<>(e.toString());
        }
    }

    @Operation(summary = "게시글 리스트 조회", description = "게시글 리스트를 좋아요가 많은 순대로 가지고 옵니다. ")
    @GetMapping("/list")
    public BaseResponse<String> GetArticleList(){



        return null;
    }




}
