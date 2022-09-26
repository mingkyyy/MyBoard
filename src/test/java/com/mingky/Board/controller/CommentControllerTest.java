package com.mingky.Board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mingky.Board.domain.*;
import com.mingky.Board.dto.CommentDto;
import com.mingky.Board.dto.CommentUpdateDto;
import com.mingky.Board.repository.CommentRepository;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    @LocalServerPort
    private int port;

    private MockMvc mvc;


    @Autowired
    private PostRepository postRepository;


    @Autowired
    private WebApplicationContext context;


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(Member.builder()
                        .memberType(MemberType.ROLE_MEMBER)
                        .nickname("text")
                        .name("홍길동")
                        .tel("01012341234")
                        .email("test@test.com")
                        .password("12345")
                .build());

        postRepository.save(Post.builder()
                        .content("content")
                        .title("title")
                        .category(Category.FREE)
                        .write(memberRepository.findAll().get(0))
                .build());
    }

    @AfterEach
    public void tearDown() throws Exception {
        commentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    public void 댓글_등록() throws Exception {
        Long postId = postRepository.findAll().get(0).getId();
        String comment = "댓글";

        CommentDto commentDto = CommentDto.builder()
                .comment(comment)
                .parentComment(-1L)
                .build();

        String url = "https://localhost:" + port + "/board/comment/" + postId;

        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentDto))
                )
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.get(0).getComment()).isEqualTo(comment);

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 댓글_삭제() throws Exception{
                commentRepository.save(Comment.builder()
                        .comment("댓글")
                        .parentComment(-1L)
                        .commentWriter(memberRepository.findAll().get(0))
                        .post(postRepository.findAll().get(0))
                .build());

                Long commentId = commentRepository.findAll().get(0).getId();

                String url = "http://localhost:" + port + "/board/comment/" + commentId;

                mvc.perform(MockMvcRequestBuilders.delete(url))
                        .andExpect(status().isOk());

                List<Comment> commentList = commentRepository.findAll();
                assertThat(commentList.isEmpty());
    }

    @Test
    @WithMockUser(roles="MEMBER")
    public void 댓글_수정()throws Exception{

        commentRepository.save(Comment.builder()
                .comment("댓글")
                .parentComment(-1L)
                .commentWriter(memberRepository.findAll().get(0))
                .post(postRepository.findAll().get(0))
                .build());

        Long commentId = commentRepository.findAll().get(0).getId();

        String newComment = "새로운 댓글";

        CommentUpdateDto commentUpdateDto = CommentUpdateDto.builder()
                .comment(newComment)
                .build();

        String url = "http://localhost:" + port + "/board/comment/" +commentId;

        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                        .writeValueAsString(commentUpdateDto))
        ).andExpect(status().isOk());

        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getComment()).isEqualTo(newComment);

    }
}
