package com.mingky.Board.util;

import com.mingky.Board.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberUser extends User {

    private final Member member;

    public MemberUser(Member member){
        super(member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getMemberType().name()))
        );

    this.member = member;
    }

    @Override
    public String getUsername() {
        return member.getNickname();
    }
}
