package com.kdom.backend.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kdom.backend.converter.ArticleConverter;
import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Hashtag;
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

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String uploadImage(MultipartFile multipartFile) throws IOException {

        AmazonS3 s3Client = AmazonS3Client.builder().withRegion("ap-northeast-2").build();
        String fileName = multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        s3Client.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

        return s3Client.getUrl(bucket, fileName).toString();
    }

    @Override
    public String uploadArticle(String title, String content, String imageUrl, String linkUrl, List<String> keyword, String target) {

        Article article = new Article(title, content, imageUrl, linkUrl, target);
        articleRepository.save(article);
        if (keyword.size() != 3) {
            for (int i = 0; i <= 4 - keyword.size(); i++) { //로직 개선 필요
                System.out.println("**");
                keyword.add(null);
            }
        }
        Hashtag hashtag = new Hashtag(keyword.get(0), keyword.get(1), keyword.get(2), article);
        //hashtag 개수가 null일 때는 응답이 되지 않아 존재하는 개수에 따라 응답 형태가 달라진다.
        //응답 형태가 다른게 골치 아플까 null로 반환하는 것이 골치아플까?
        hashtagRepository.save(hashtag);

        if (article != null && hashtag != null) {
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public ArticleResponseDto.GetArticleDetail findArticleDetail(Long articleId) {

        List<String> keywords = new ArrayList<>();

        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new BusinessException(EMPTYARTICLE)
        );

        Hashtag hashtag = hashtagRepository.findByArticleId(articleId).orElseThrow(
                () -> new BusinessException(EMPTYHASHTAG)
        );

        Integer count = likeRepository.countByArticleId(articleId);

        if (hashtag.getKeyword2() != null) {
            keywords.add(hashtag.getKeyword2());
        }
        if (hashtag.getKeyword3() != null) {
            keywords.add(hashtag.getKeyword3());
        }
        return ArticleConverter.toArticleDto(article, keywords, count);
    }

    @Override
    public ArticleResponseDto.GetArticleDetailList findArticleList(Long articleId){
        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findByIdLessThanOrderByIdDesc(articleId, pageable);
        return makeArticleList(articleList);
    }

    @Override
    public ArticleResponseDto.GetArticleDetailList findArticleFirstList(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findTop10ByOrderByIdDesc();
        return makeArticleList(articleList);
    }

    @Override
    public ArticleResponseDto.GetArticleDetailList findArticleListByTarget(Long articleId, String target){
        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articleList = articleRepository.findByTargetAndIdLessThanOrderByIdDesc(target, articleId, pageable);
        return makeArticleList(articleList);
    }

    public ArticleResponseDto.GetArticleDetailList makeArticleList(List<Article> articleList){
        List<ArticleResponseDto.GetArticleDetail> responseList = new ArrayList<>();

        if (articleList.isEmpty()){
            throw new BusinessException(NOMOREARTICLE);
        }

        for(int i=0; i<articleList.size(); i++){

            List<String> keywords = new ArrayList<>();

            Hashtag hashtag = hashtagRepository.findByArticleId(articleList.get(i).getId()).orElseThrow(
                    ()-> new BusinessException(EMPTYHASHTAG)
            );

            if(hashtag.getKeyword2()!=null) {
                keywords.add(hashtag.getKeyword2());
            }
            if(hashtag.getKeyword3()!=null) {
                keywords.add(hashtag.getKeyword3());
            }

            Integer count = likeRepository.countByArticleId(articleList.get(i).getId());

            responseList.add(ArticleConverter.toArticleDto(articleList.get(i), keywords, count));
        }
        return ArticleConverter.toArticleDtoList(responseList);
    }

    @Override
    public ArticleResponseDto.GetArticleDetailList findArticleRankList (Long article_id){

        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleResponseDto.GetArticleDetail> articleDetailList = new ArrayList<>();
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

            articleDetailList.add(ArticleConverter.toArticleDto(articleList.get(i), keywords, count));
        }
        return ArticleConverter.toArticleDtoList(articleDetailList);

    }

    @Override
    public ArticleResponseDto.GetTargetDtoList findArticleTargetRankList (Long article_id){

        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleResponseDto.GetTargetDto> articleTargetDetailList = new ArrayList<>();
        List<Article> articleList = new ArrayList<>(articleRepository.findAllByNameRank(article_id, pageable));

        for (int i = 0; i < articleList.size(); i++) {
            Integer count = articleRepository.countByTarget(articleList.get(i).getTarget());
            articleTargetDetailList.add(ArticleConverter.toTargetDto(articleList.get(i), count));
        }

        return ArticleConverter.toTargetDtoList(articleTargetDetailList);
    }
}
