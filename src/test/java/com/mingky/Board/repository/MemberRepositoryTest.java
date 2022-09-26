package com.mingky.Board.repository;


import com.mingky.Board.domain.Member;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    public void 멤버리파지토리_테스트하기() {
        String name = "김민경";
        String email = "mingkyy12@gmail.com";
        String password = "123456";
        String nickname = "mingkyy";

        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());

        List<Member> memberList = memberRepository.findAll();

        Member member = memberList.get(0);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getNickname()).isEqualTo(nickname);
    }
}
