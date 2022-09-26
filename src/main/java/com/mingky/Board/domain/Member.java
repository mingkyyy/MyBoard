package com.mingky.Board.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String tel;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @JsonManagedReference
    @OneToMany(mappedBy = "write", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "commentWriter", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "reportMember", cascade = CascadeType.ALL)
    private List<Report> reportList;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Post> like;

    @PostLoad
    public void createList(){
        if (posts == null) posts = new ArrayList<>();
        if (like == null) like = new ArrayList<>();
        if (comments == null) comments = new ArrayList<>();
        if (reportList == null) reportList = new ArrayList<>();
    }

    public void addLike(Post post) {
        like.add(post);
    }

    public void findName(String name){
        this.name = name;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeTel(String tel) {
        this.tel = tel;
    }
}
