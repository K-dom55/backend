package com.kdom.backend.controller;

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

import javax.validation.constraints.NotNull;
import java.io.IOException;


import static com.kdom.backend.exception.ExceptionCode.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 글 작성 API
     * [POST] /article
     * @return BaseResponse<String>
     * */

    @Operation(summary = "게시글 작성", description = "multi file과 dto를 swagger에서 동시에 보낼 수 없어 swagger를 참고로 postman을 사용해주길 바랍니다. ")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 이거 cunsumes설정 이렇게 해줘야 swagger에서 파일 직접 선택 할 수 있다 ㅜㅜ
    public BaseResponse<String> PostArticle(@Validated @RequestPart("dto") @Parameter() ArticleRequestDto.postArticleDto request , @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @Validated @RequestPart("file") MultipartFile multiPartFile){
        try {
            String S3Url = articleService.uploadImage(multiPartFile);
            if(S3Url==null || S3Url.isEmpty() || S3Url.isBlank()){
                return new BaseResponse<>(EMPTYS3URL);
            }
            String success = articleService.uploadArticle(request.getTitle(), request.getContent(), S3Url, request.getLinkUrl(), request.getKeyword(),request.getTarget());
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
    @GetMapping
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


    // 리스트 조회 > 검색 가능, 최신 순으로
    // 랭킹 리스트 조회 > 검색 없음, 두 가지 기준(최애 언급 기준, 좋아요 많은순), 100위까지

    @Operation(summary = "게시글 리스트 조회", description = "게시글 리스트를 최신 순으로 가지고 옵니다. ")
    @GetMapping("/list")
    public BaseResponse<ArticleResponseDto.GetArticleDetailList> GetArticleList(@Validated @NotNull Long articleId, String target_name, String title_name ){

        ArticleResponseDto.GetArticleDetailList ArticleDetail = articleService.findArticleList(articleId, target_name, title_name);

        return new BaseResponse<>(ArticleDetail);
    }

    @Operation(summary = "게시글 랭킹 리스트 조회", description = "좋아요가 많은 순")
    @GetMapping("/rank/like")
    public BaseResponse<ArticleResponseDto.GetArticleDetailList> GetArticleRankList(@Validated @NotNull Long article_id){

        ArticleResponseDto.GetArticleDetailList ArticleDetail= articleService.findArticleRankList(article_id);

        return new BaseResponse<>(ArticleDetail);
    }

    @Operation(summary = "게시글 랭킹 리스트 조회 (언급된 순위)", description = "언급량이 많은 순")
    @GetMapping("/rank/target")
    public BaseResponse<ArticleResponseDto.GetTargetDtoList> GetArticleTargetRankList(@Validated @NotNull Long article_id){

        ArticleResponseDto.GetTargetDtoList ArticleDetail= articleService.findArticleTargetRankList(article_id);

        return new BaseResponse<>(ArticleDetail);
    }
}
