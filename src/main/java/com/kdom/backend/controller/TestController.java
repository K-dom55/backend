package com.kdom.backend.controller;

import com.kdom.backend.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Operation(summary = "Health Check",description = "Server가 정상적으로 동작하는지 확인한다.")
    @GetMapping("/")
    public BaseResponse<String> HealthCheck(){

        System.out.println("정상");
        return new BaseResponse<>("정상 출력되었습니다.");
    }

    @GetMapping("/check")
    public String HealthCheck2(){
        return "배포 확인용";
    }
}
