package com.kdom.backend.controller;

import com.kdom.backend.dto.request.ArticleRequestDto;
import com.kdom.backend.dto.response.ArticleDetailResponseDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.exception.BaseResponse;
import com.kdom.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static com.kdom.backend.exception.ExceptionCode.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 글 작성 API
     * [POST] /article
     * @return BaseResponse<String>
     * */

    @Operation(summary = "이미지 저장 api")
    @PostMapping(value = "/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<String> PostImage(@RequestPart("file") MultipartFile multipartFile){
        try {
            String s3Url = articleService.uploadImage(multipartFile);

            return new BaseResponse<>(s3Url);
        }catch (IOException e){
            return new BaseResponse<>("err");
        }

    }


    @Operation(summary = "게시글 작성", description = "multi file과 dto를 swagger에서 동시에 보낼 수 없어 swagger를 참고로 postman을 사용해주길 바랍니다. ")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 이거 cunsumes설정 이렇게 해줘야 swagger에서 파일 직접 선택 할 수 있다 ㅜㅜ
    public BaseResponse<ArticleResponseDto> PostArticle(@Validated @RequestPart("dto") @Parameter() ArticleRequestDto.postArticleDto request , @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @Validated @RequestPart("file") MultipartFile multiPartFile){
        try {
            String S3Url = articleService.uploadImage(multiPartFile);
            if(S3Url==null || S3Url.isBlank()){
                return new BaseResponse<>(EMPTYS3URL);
            }
            ArticleResponseDto success = articleService.uploadArticle(request.getTitle(), request.getContent(), S3Url, request.getLinkUrl(), request.getKeyword(),request.getTarget());
            if(success==null){
                return new BaseResponse<>(ERRARTICLEREPO);
            }
            return new BaseResponse<>(success);
        }catch (IOException i){
            return new BaseResponse<>(IOEXCEPTION);

        }
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회를 위한 API입니다.")
    @GetMapping("/{articleId}")
    public BaseResponse<ArticleDetailResponseDto> GetArticle(@Validated @PathVariable Long articleId){
        try {
            ArticleDetailResponseDto getArticleDetail = articleService.findArticleDetail(articleId);

            if (getArticleDetail == null) {
                return new BaseResponse<>(EMPTYARTICLEDTO);
            }

            return new BaseResponse<>(getArticleDetail);
        }catch(Exception e){
            return new BaseResponse<>(e.toString());
        }
    }

    @Operation(summary = "게시글 리스트 조회", description = "무한스크롤에 사용될 게시글 리스트를 최신 순으로 가지고 옵니다. ")
    @GetMapping()
    public BaseResponse<List<ArticleDetailResponseDto>> GetArticleList(@Validated @RequestParam(name = "param", required = false) Optional<Long> articleId){
        if(articleId.isPresent()){
            return new BaseResponse<>( articleService.findArticleList(articleId.get()));
        } else {
            return new BaseResponse<>(articleService.findArticleFirstList());
        }
    }

    @Operation(summary = "게시글 리스트 존함으로 검색", description = "검색한 게시글 리스트를 최신 순으로 가지고 옵니다. ")
    @GetMapping("/search")
    public BaseResponse<List<ArticleDetailResponseDto>> GetArticleListByTarget(@Validated @RequestParam(name = "param", required = false) Optional<Long> articleId, String targetName){
        if(articleId.isPresent()){
            return new BaseResponse<>(articleService.findArticleListByTarget(articleId.get(), targetName));
        } else {
            return new BaseResponse<>(articleService.findArticleFirstListByTarget(targetName));
        }
    }

    /*
    @Operation(summary = "게시글 랭킹 리스트 조회", description = "좋아요가 많은 순")
    @GetMapping("/rank/like")
    public BaseResponse<List<ArticleDetailResponseDto>> GetArticleRankList(@Validated @NotNull Long article_id){
        return new BaseResponse<>(articleService.findArticleRankList(article_id));
    }

    @Operation(summary = "게시글 랭킹 리스트 조회 (언급된 순위)", description = "언급량이 많은 순")
    @GetMapping("/rank/target")
    public BaseResponse<List<ArticleTargetListResponseDto>> GetArticleTargetRankList(@Validated @NotNull Long article_id){
        return new BaseResponse<>(articleService.findArticleTargetRankList(article_id));
<<<<<<< HEAD
    }
    */

}
