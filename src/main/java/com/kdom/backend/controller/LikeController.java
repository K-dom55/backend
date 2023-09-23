package com.kdom.backend.controller;

import com.kdom.backend.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

@RequestMapping("/api/likes")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가", description = "사용자가 좋아요 버튼을 누르면, 게시글에 좋아요를 추가한다.")
    @PostMapping("/{articleId}")
    public void addLike(@PathVariable Long articleId) {
        likeService.addLike(articleId, getMacAddress());
    }

    private String getMacAddress() {
        String result = "";
        InetAddress ip;

        try {
            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            result = sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }

        return result;
    }
}
