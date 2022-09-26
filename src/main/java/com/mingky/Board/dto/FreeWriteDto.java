package com.mingky.Board.dto;

import com.mingky.Board.domain.Category;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FreeWriteDto {

    private String title;

    private String content;

    private Member member;

    private Category category;

    @Builder
    public FreeWriteDto(Category category,String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;


    }
    public Post freeWrite(){
        return Post.builder()
                .write(member)
                .content(content)
                .title(title)
                .category(Category.FREE)
                .build();
    }

}
