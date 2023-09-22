package com.kdom.backend.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Hashtag;
import com.kdom.backend.repository.ArticleRepository;
import com.kdom.backend.repository.HashtagRepository;
import com.kdom.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final AmazonS3 amazonS3;

    private final ArticleRepository articleRepository;

    private final HashtagRepository hashtagRepository;

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
            for(int i =  0; i <= 3 - keyword.size(); i++) {
                System.out.println("**");
                keyword.add(null);
            }
        }
        Hashtag hashtag = new Hashtag(keyword.get(0),keyword.get(1), keyword.get(2), article);
        hashtagRepository.save(hashtag);

        if(article!=null && hashtag!=null){
            return "SUCCESS";
        }
        return null;
    };



}
