package com.mingky.Board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {
    private String comment;

    private Long parentComment;


    @Builder
    public CommentDto(String comment, Long parentComment){
        this.comment = comment;
        this.parentComment = parentComment;

    }
}
