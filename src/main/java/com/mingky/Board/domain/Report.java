package com.mingky.Board.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportText;

    @ManyToOne
    private Post reportPost;

    @ManyToOne
    private Member reportMember;

    public void reportSave(String reportText, Post reportPost, Member reportMember){
        this.reportText = reportText;
        this.reportPost = reportPost;
        this.reportMember = reportMember;
    }


}
