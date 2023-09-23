package com.kdom.backend.service;

import com.kdom.backend.domain.Article;
import com.kdom.backend.domain.Likes;
import com.kdom.backend.domain.Member;
import com.kdom.backend.exception.BusinessException;
import com.kdom.backend.exception.ExceptionCode;
import com.kdom.backend.repository.ArticleRepository;
import com.kdom.backend.repository.LikeRepository;
import com.kdom.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;

    public void addLike(Long articleId, String macAddress) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_ID_NOT_FOUND));

        if (likeRepository.existsByArticle_IdAndMember_MacAddress(articleId, macAddress)) {
            throw new BusinessException(ExceptionCode.LIKE_USER_IP_CONFLICT);
        }

        Member member = Member.builder().macAddress(macAddress).build();
        Member savedMember;

        if (!memberRepository.existsByMacAddress(macAddress)) {
            memberRepository.save(member);
            Likes like = Likes.builder().member(member).article(article).build();
            likeRepository.save(like);
        } else {
            savedMember = memberRepository.findByMacAddress(macAddress)
                    .orElseThrow(() -> new BusinessException(ExceptionCode.MEMBER_IP_NOT_FOUND));
            Likes like = Likes.builder().member(savedMember).article(article).build();
            likeRepository.save(like);
        }
    }
}
