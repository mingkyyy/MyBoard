package com.mingky.Board.service;

import com.mingky.Board.domain.Comment;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.MemberType;
import com.mingky.Board.domain.Post;
import com.mingky.Board.dto.SignupDto;
import com.mingky.Board.repository.CommentRepository;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.util.MemberUser;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @Transactional
    public Member save(SignupDto signupDto) {
        return memberRepository.save(signupDto.toEntity());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(username);
        Member member = optional.orElseThrow(
                () -> new UsernameNotFoundException("미등록 계정")
        );

        return new MemberUser(member);
    }

    public void login(Member member) {
        MemberUser user = new MemberUser(member);

        // 유저 정보를 담은 인증 토큰 생성
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getMember().getPassword(),
                        user.getAuthorities()
                );

        // 인증 토큰을 SecurityContext 에 저장. <~ 로그인 되었다!
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {
        String api_key = "NCSNIGG9KCSOYLFV";
        String api_secret = "DHZMV53VX708ZI81TNFA6AA6C29SGS0O";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", " 01053196261");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "Board 휴대폰인증 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    @Transactional
    public String updateNickname(Long id,String nickname) {
        Member member = memberRepository.findById(id).orElseThrow();

        if ( memberRepository.existsByNickname(nickname) == true ){
            return "duplicate";
        } else {
            member.changeNickname(nickname);
           return "ok";
        }

    }

    @Transactional
    public String updateTel(Long id,String tel) {
        Member member = memberRepository.findById(id).orElseThrow();

        if ( memberRepository.existsByTel(tel) == true ){
            return "duplicate";
        } else {
            member.changeTel(tel);
            return "ok";
        }

    }

    public Long deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        List<Comment> commentList = member.getComments();
        List<Post> postList =  member.getLike();

        if (commentList != null){
            for (int i =0; i<commentList.size(); i++){
                commentRepository.delete(commentList.get(i));
            }
        }

        if (postList != null){
            for (int i =0; i<postList.size(); i++){
               member.getLike().remove(postList.get(i));
            }
        }


        memberRepository.delete(member);
        SecurityContextHolder.clearContext();
        return id;
    }
}
