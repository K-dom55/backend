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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * @Input MultipartFile 파일, String 폴더명
     * @Output String 파일 url
     * Multipart 파일을 File로 전환한 후 업로드하는 함수
     * */

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {

        AmazonS3 s3Client = AmazonS3Client.builder().withRegion("ap-northeast-2").build();
        String fileName = multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        s3Client.putObject(bucket, fileName, multipartFile.getInputStream(),objectMetadata);

        return s3Client.getUrl(bucket, fileName).toString();


    }

    @Override
    public String uploadArticle(String title, String content, String imageUrl, String linkUrl, List<String> keyword){

        Article article = new Article(title, content, imageUrl, linkUrl );
        articleRepository.save(article);
        if(keyword.size()!=3){
            System.out.println(keyword.size());
            for(int i =  0; i <= 4 - keyword.size(); i++) { //로직 개선 필요
                System.out.println("**");
                keyword.add(null);
            }
        }
        Hashtag hashtag = new Hashtag(keyword.get(0),keyword.get(1), keyword.get(2), article);
        //hashtag 개수가 null일 때는 응답이 되지 않아 존재하는 개수에 따라 응답 형태가 달라진다.
        //응답 형태가 다른게 골치 아플까 null로 반환하는 것이 골치아플까?
        hashtagRepository.save(hashtag);

        if(article!=null && hashtag!=null){
            return "SUCCESS";
        }
        return null;
    };

    @Override
    public ArticleResponseDto.GetArticleDetail findArticleDetail(Long articleId){

        Article article = articleRepository.findById(articleId).orElseThrow(
                ()-> new BusinessException(EMPTYARTICLE)
        );

        if(article.getId()==null){
            throw new BusinessException(EMPTYARTICLE);
        }

        Hashtag hashtag = hashtagRepository.findByArticleId(articleId).orElseThrow(
                ()-> new BusinessException(EMPTYHASHTAG)
        );

        Integer count = likeRepository.countByArticleId(articleId);

        if(count==null){
            count = 0;
        }

        List<String> keywords = new ArrayList<>();
        if(hashtag.getKeyword1()!=null) {
            keywords.add(hashtag.getKeyword1());
        }
        if(hashtag.getKeyword2()!=null) {
            keywords.add(hashtag.getKeyword2());
        }
        if(hashtag.getKeyword3()!=null) {
            keywords.add(hashtag.getKeyword3());
        }
        return ArticleConverter.toArticleDto(article, keywords, count);
    }

    @Override
    public ArticleResponseDto.GetArticleDetailList findArticleList(Long articleId, String target_name, String title_name){

        Pageable pageable = PageRequest.of(0, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "article_id");
        List<Article> articleList = articleRepository.findByIdLessThanOrderByIdDesc(articleId, pageable);
        List<ArticleResponseDto.GetArticleDetail> responseList = new ArrayList<>();

        if (articleList.isEmpty()){
            throw new BusinessException(NOMOREARTICLE);
        }

        for(int i=0; i<articleList.size(); i++){

            Hashtag hashtag = hashtagRepository.findByArticleId(articleId).orElseThrow(
                    ()-> new BusinessException(EMPTYHASHTAG)
            );

            List<String> keywords = new ArrayList<>();
            if(hashtag.getKeyword1()!=null) {
                keywords.add(hashtag.getKeyword1());
            }
            if(hashtag.getKeyword2()!=null) {
                keywords.add(hashtag.getKeyword2());
            }
            if(hashtag.getKeyword3()!=null) {
                keywords.add(hashtag.getKeyword3());
            }

            Integer count = likeRepository.countByArticleId(articleId);
            if(count==null){
                count = 0;
            }
            responseList.add(ArticleConverter.toArticleDto(articleList.get(i), keywords, count));
        }
        return ArticleConverter.toArticleDtoList(responseList);
    }
}
