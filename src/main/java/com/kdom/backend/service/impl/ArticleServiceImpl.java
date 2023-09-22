package com.kdom.backend.service.impl;

import com.kdom.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    /**
     * 파일 업로드
     * @Input MultipartFile 파일, String 폴더명
     * @Output String 파일 url
     * Multipart 파일을 File로 전환한 후 업로드하는 함수
     * */

    @Override
    public String uploadImage(MultipartFile multipartFile){

        String fileName = multipartFile.getOriginalFilename();

        //Object

        return null;
    }



}
