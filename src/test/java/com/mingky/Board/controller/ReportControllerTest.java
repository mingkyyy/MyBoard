package com.mingky.Board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mingky.Board.domain.*;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.repository.ReportRepository;
import com.mingky.Board.service.MemberService;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTest {


    @LocalServerPort
    private int port;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReportRepository reportRepository;


    @Autowired
    private WebApplicationContext context;


    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void creanReport() throws Exception{
        reportRepository.deleteAll();
    }

    @BeforeEach
    public void createRepository() throws Exception{
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        memberRepository.save(Member.builder()
                        .tel("01012341234")
                        .name("text")
                        .email("test@test.com")
                        .nickname("testnickname")
                        .memberType(MemberType.ROLE_MANAGE)
                        .password(passwordEncoder.encode("test12345!"))
                .build());

        postRepository.save(Post.builder()
                .content("content")
                .title("title")
                .category(Category.FREE)
                .write(memberRepository.findAll().get(0))
                .build());

    }

    @Test
    @WithMockUser(roles = "MANAGE")
    public void 신고_삭제()throws Exception{

        reportRepository.save(Report.builder()
                        .reportPost(postRepository.findAll().get(0))
                        .reportMember(memberRepository.findAll().get(0))
                        .reportText("내용")
                .build());

        Long reportId = reportRepository.findAll().get(0).getId();

        String url = "https://localhost:" + port + "/report/" + reportId;

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList.isEmpty());


    }

}
