package com.kdom.backend.domain;

import com.kdom.backend.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "LIKES")
@Entity
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = true)
    private Article article;

    @Builder
    public Likes(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
