package com.kdom.backend.controller;

import com.kdom.backend.exception.BaseResponse;
import com.kdom.backend.repository.ArticleRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController("/test")
public class TestController {

    private final ArticleRepository articleRepository;

    @Operation(summary = "Health Check",description = "Server가 정상적으로 동작하는지 확인한다.")
    @GetMapping("/")
    public BaseResponse<String> HealthCheck(){

        System.out.println("정상");
        return new BaseResponse<>("정상 출력되었습니다.");
    }

    @GetMapping("/rank")
    public BaseResponse<String> TestRank(Long id){

        org.springframework.data.domain.Pageable pageable = PageRequest.of(0,10);




        return new BaseResponse<>("null");
    }
}
