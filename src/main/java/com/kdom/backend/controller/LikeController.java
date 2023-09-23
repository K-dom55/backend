package com.kdom.backend.controller;

import com.kdom.backend.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/like")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가", description = "사용자가 좋아요 버튼을 누르면, 게시글에 좋아요를 추가한다.")
    @PostMapping("/{articleId}")
    public void addLike(@PathVariable Long articleId, HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getRemoteAddr();
        likeService.addLike(articleId, ip);
    }
}
