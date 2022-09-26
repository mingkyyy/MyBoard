package com.mingky.Board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mingky.Board.domain.*;
import com.mingky.Board.dto.FreeUpdateDto;
import org.apache.struts.mock.MockHttpSession;
import com.mingky.Board.dto.FreeWriteDto;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {

    @LocalServerPort
    private int port;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostService postService;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Member member = Member.builder()
                .name("mingkyy")
                .email("test@test.com")
                .nickname("mi")
                .password(passwordEncoder.encode("12345"))
                .tel("01000000000")
                .memberType(MemberType.ROLE_MEMBER)
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    public void tearDown() throws Exception {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 게시물_등록() throws Exception {
        String title = "title";
        String content = "content";

        List<Member> memberList = memberRepository.findAll();

        FreeWriteDto freeWriteDto = FreeWriteDto.builder()
                .title(title)
                .content(content)
                .category(Category.FREE)
                .member(memberList.get(0))
                .build();

        String url = "http://localhost:" + port + "/free/write";

        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                        .writeValueAsString(freeWriteDto))
        ).andExpect(status().isOk());

        Post post = postRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 게시물_삭제() throws Exception {
        List<Member> memberList = memberRepository.findAll();

        postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .category(Category.FREE)
                .write(memberList.get(0))
                .build());

        Long postId = postRepository.findAll().get(0).getId();

        String url = "http://localhost:" + port + "/board/free/read/" + postId;

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk());

        List<Post> postList = postRepository.findAll();
        assertThat(postList.isEmpty());


    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 게시물_수정() throws Exception {
        List<Member> memberList = memberRepository.findAll();

        postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .category(Category.FREE)
                .write(memberList.get(0))
                .build());

        Long postId = postRepository.findAll().get(0).getId();

        String newTitle = "newTitle";
        String newContent = "newContent";

        FreeUpdateDto freeUpdateDto = FreeUpdateDto
                .builder()
                .title(newTitle)
                .content(newContent)
                .build();

        String url = "http://localhost:" + port + "/board/free/read/" + postId;

        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                        .writeValueAsString(freeUpdateDto))
        ).andExpect(status().isOk());

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(newTitle);
        assertThat(all.get(0).getContent()).isEqualTo(newContent);

    }



}
