package com.kdom.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArticleService {

    /**
     * 파일 업로드
     * @Input MultipartFile 파일
     * @Output String 파일 url
     * ~한 함수
     * */

    String uploadImage(MultipartFile multipartFile) throws IOException;


}


