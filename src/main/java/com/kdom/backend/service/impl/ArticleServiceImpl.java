package com.kdom.backend.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kdom.backend.converter.ArticleConverter;
import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Hashtag;
import com.kdom.backend.dto.request.ArticleRequestDto;
import com.kdom.backend.dto.response.ArticleDetailResponseDto;
import com.kdom.backend.dto.response.ArticleResponseDto;
import com.kdom.backend.exception.BusinessException;
import com.kdom.backend.repository.ArticleRepository;
import com.kdom.backend.repository.HashtagRepository;
import com.kdom.backend.repository.LikeRepository;
import com.kdom.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.kdom.backend.exception.ExceptionCode.*;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final AmazonS3 amazonS3;

    private final ArticleRepository articleRepository;

    private final HashtagRepository hashtagRepository;

    private final LikeRepository likeRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드
     *
     * @Input MultipartFile 파일, String 폴더명
     * @Output String 파일 url
     * Multipart 파일을 File로 전환한 후 업로드하는 함수
     */
    @Override
    public String uploadImage(MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAIL);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Override
    public ArticleResponseDto uploadArticle(ArticleRequestDto dto) {
        Article article = ArticleConverter.toArticle(dto);
        Article savedArticle = articleRepository.save(article);
        Hashtag hashtag = createHashtag(dto.keywords, article);
        hashtagRepository.save(hashtag);
        return ArticleConverter.toArticleDto(savedArticle, createKeywords(savedArticle));
    }

    @Override
    public ArticleDetailResponseDto findArticleDetail(Long articleId) {

        List<String> keywords = new ArrayList<>();

        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new BusinessException(EMPTYARTICLE)
        );

        Hashtag hashtag = hashtagRepository.findByArticleId(articleId).orElseThrow(
                () -> new BusinessException(EMPTYHASHTAG)
        );

        Integer count = likeRepository.countByArticleId(articleId);

        return ArticleConverter.toArticleDetailDto(article, createKeywords(article), count, null);
    }

    @Override
    public List<ArticleDetailResponseDto> findArticleList(Long articleId){

        Pageable pageable = PageRequest.of(0, 10);

        List<Object[]> articleList =  articleRepository.findRankbyArticleId(articleId, pageable);
        List<Article> resultArticleList = new ArrayList<>();
        List<Integer> resultRankList = new ArrayList<>();
        for(int i = 0; i<articleList.size();i++){
            Long ids = ((Number) articleList.get(i)[0]).longValue();

            Article article = Article.builder()
                    .id(ids)
                    .title((String) articleList.get(i)[7])
                    .content((String) articleList.get(i)[2])
                    .imgUrl((String) articleList.get(i)[3])
                    .linkUrl((String) articleList.get(i)[5])
                    .target((String) articleList.get(i)[6])
                    .build();

            resultArticleList.add(article);
            BigDecimal decimal = (BigDecimal) articleList.get(i)[8];
            resultRankList.add(decimal.toBigInteger().intValue());
        }

        return makeArticleList(resultArticleList, resultRankList);
    }

    @Override
    public List<ArticleDetailResponseDto> findArticleFirstList(){

        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findTop10ByOrderByIdDesc();
        List<Integer> rankList = articleRepository.findRank();

        return makeArticleList(articleList,rankList);
    }

    @Override
    public List<ArticleDetailResponseDto> findArticleListByTarget(Long articleId, String target){

        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findByTargetAndIdLessThanOrderByIdDesc(target, articleId, pageable);

        return makeArticleList(articleList);
    }

    @Override
    public List<ArticleDetailResponseDto> findArticleFirstListByTarget(String target){

        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findTop10ByTargetContainingOrderByIdDesc(target);

        return makeArticleList(articleList);
    }

    public List<ArticleDetailResponseDto> makeArticleList(List<Article> articleList){

        List<ArticleDetailResponseDto> responseList = new ArrayList<>();
        if (articleList.isEmpty()){
            throw new BusinessException(NOMOREARTICLE);
        }

        for(int i=0; i<articleList.size(); i++){
            Hashtag hashtag = hashtagRepository.findByArticleId(articleList.get(i).getId()).orElseThrow(
                    ()-> new BusinessException(EMPTYHASHTAG)
            );
            Integer count = likeRepository.countByArticleId(articleList.get(i).getId());
            Integer rank = 0;
            responseList.add(ArticleConverter.toArticleDetailDto(articleList.get(i), createKeywords(articleList.get(i)), count,rank));
        }
        return responseList;
    }

    public List<ArticleDetailResponseDto> makeArticleList(List<Article> articleList, List<Integer> rankList){

        List<ArticleDetailResponseDto> responseList = new ArrayList<>();
        if (articleList.isEmpty()){
            throw new BusinessException(NOMOREARTICLE);
        }

        for(int i=0; i<articleList.size(); i++){
            Hashtag hashtag = hashtagRepository.findByArticleId(articleList.get(i).getId()).orElseThrow(
                    ()-> new BusinessException(EMPTYHASHTAG)
            );
            Integer count = likeRepository.countByArticleId(articleList.get(i).getId());
            Integer rank = 0;
            responseList.add(ArticleConverter.toArticleDetailDto(articleList.get(i), createKeywords(articleList.get(i)), count,rankList.get(i)));
        }
        return responseList;
    }

    /*
    @Override
    public List<ArticleDetailResponseDto> findArticleRankList (Long article_id){

        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleDetailResponseDto> articleDetailList = new ArrayList<>();
        List<Article> articleList = new ArrayList<>(articleRepository.findAllByLikeRank(article_id, pageable));

        for (int i = 0; i < articleList.size(); i++) {

            List<String> keywords = new ArrayList<>();
            Hashtag hashtag = hashtagRepository.findByArticleId(articleList.get(i).getId())
                    .orElseThrow(() -> new BusinessException(EMPTYHASHTAG));
            Integer count = likeRepository.countByArticleId(articleList.get(i).getId());

            if (hashtag.getKeyword2() != null) {
                keywords.add(hashtag.getKeyword2());
            }
            if (hashtag.getKeyword3() != null) {
                keywords.add(hashtag.getKeyword3());
            }

            articleDetailList.add(ArticleConverter.toArticleDto(articleList.get(i), keywords, count,null));
        }
        return articleDetailList;
    }


    @Override
    public List<ArticleTargetListResponseDto> findArticleTargetRankList (Long article_id){

        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleTargetListResponseDto> responseDtoList = new ArrayList<>();
        List<Article> articleList = new ArrayList<>(articleRepository.findAllByNameRank(article_id, pageable));

        for (int i = 0; i < articleList.size(); i++) {
            Integer count = articleRepository.countByTarget(articleList.get(i).getTarget());
            responseDtoList.add(ArticleConverter.toTargetDto(articleList.get(i), count));
        }

        return responseDtoList;
    }

    */

    /*@Override
    public ArticleResponseDto.GetTargetDtoList findArticleTargetRankList (Long article_id){

        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleResponseDto.GetTargetDto> articleTargetDetailList = new ArrayList<>();
        List<Article> articleList = new ArrayList<>(articleRepository.findAllByNameRank(article_id, pageable));

        for (int i = 0; i < articleList.size(); i++) {
            Integer count = articleRepository.countByTarget(articleList.get(i).getTarget());
            articleTargetDetailList.add(ArticleConverter.toTargetDto(articleList.get(i), count));
        }

        return ArticleConverter.toTargetDtoList(articleTargetDetailList);
    }*/


    private List<String> createKeywords(Article article){

        Hashtag hashtag = hashtagRepository.findByArticleId(article.getId()).orElseThrow(
                () -> new BusinessException(EMPTYHASHTAG)
        );

        List<String> keywords = new ArrayList<>();

        keywords.add(hashtag.getKeyword1());
        if (hashtag.getKeyword2() != null) {
            keywords.add(hashtag.getKeyword2());
        }
        if (hashtag.getKeyword3() != null) {
            keywords.add(hashtag.getKeyword3());
        }

        return keywords;
    }

    private Hashtag createHashtag(List<String> keywords, Article article) {
        if (keywords.size() == 1) return Hashtag.builder().keyword1(keywords.get(0)).article(article).build();
        if (keywords.size() == 2) return Hashtag.builder().keyword1(keywords.get(0)).keyword2(keywords.get(1)).article(article).build();
        return Hashtag.builder().keyword1(keywords.get(0)).keyword2(keywords.get(1)).keyword3(keywords.get(2)).article(article).build();
    }
}
