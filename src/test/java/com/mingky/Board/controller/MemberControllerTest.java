package com.mingky.Board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.MemberType;
import com.mingky.Board.dto.SignupDto;
import com.mingky.Board.repository.MemberRepository;;
import com.mingky.Board.service.MemberService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;


    @Autowired
    private WebApplicationContext context;


    private MockMvc mvc;

    @AfterEach
    public void cleanMember() throws Exception {
        memberRepository.deleteAll();
    }

    @BeforeEach
    public void createRepository() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }


    @Test
    @Transactional
    public void 회원가입_테스트() throws Exception {
        String email = "test@test.com";
        String name = "mingkyy";
        String password = "test12345!";
        String nickname = "mi";
        String tel = "01012341234";


        SignupDto signupDto = SignupDto.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .tel(tel)
                .name(name)
                .build();


        Member member = memberService.save(signupDto);

        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);


    }

    @Test
    public void 로그인_테스트() throws Exception {

        memberRepository.save(Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("12345"))
                .name("test")
                .nickname("testnickname")
                .memberType(MemberType.ROLE_MEMBER)
                .build());

        String username = "test@test.com";
        String password = "12345";

        mvc.perform(formLogin()
                        .user(username)
                        .password(password))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 닉네임_변경() throws Exception {

        memberRepository.save(Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("12345"))
                .name("test")
                .nickname("testnickname")
                .memberType(MemberType.ROLE_MEMBER)
                .build());

        String newNickname = "새로운 닉네임";

        Long memberId = memberRepository.findAll().get(0).getId();

        String url = "http://localhost:" + port + "/nicknameChange/" + memberId;

        mvc.perform(MockMvcRequestBuilders.put(url)
                .param("nickname", newNickname)
        ).andExpect(status().isOk());

        Member member = memberRepository.findAll().get(0);
        assertThat(member.getNickname()).isEqualTo(newNickname);

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 번호_변경() throws Exception {

        memberRepository.save(Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("12345"))
                .name("test")
                .nickname("testnickname")
                .tel("01012341234")
                .memberType(MemberType.ROLE_MEMBER)
                .build());

        String newTel = "01045674567";

        Long memberId = memberRepository.findAll().get(0).getId();

        String url = "http://localhost:" + port + "/phoneChange/" + memberId;

        mvc.perform(MockMvcRequestBuilders.put(url)
                .param("tel", newTel)
        ).andExpect(status().isOk());

        Member member = memberRepository.findAll().get(0);
        assertThat(member.getTel()).isEqualTo(newTel);

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 회원_탈퇴()throws Exception{
        memberRepository.save(Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("12345"))
                .name("test")
                .nickname("testnickname")
                .tel("01012341234")
                .memberType(MemberType.ROLE_MEMBER)
                .build());

        Long memberId = memberRepository.findAll().get(0).getId();

        String url = "http://localhost:" + port + "/member/delete/" + memberId;

        mvc.perform(MockMvcRequestBuilders.delete(url)
        ).andExpect(status().isOk());

        List<Member> memberList =  memberRepository.findAll();
        assertThat(memberList.isEmpty());

    }


}
