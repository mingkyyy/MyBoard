package com.mingky.Board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FreeUpdateDto {

    private String title;
    private String content;

    @Builder
    public FreeUpdateDto(String title, String content){
        this.title = title;
        this.content = content;

    }
}
