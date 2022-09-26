package com.mingky.Board.controller;

import com.mingky.Board.domain.Member;
import com.mingky.Board.dto.SignupDto;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.service.MemberService;
import com.mingky.Board.util.CurrentMember;
import com.mingky.Board.util.JoinValidator;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @GetMapping("/check/sendSMS")
    public String sendSMS(String phoneNumber) {

        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        memberService.certifiedPhoneNumber(phoneNumber, numStr);
        return numStr;

    }

    @PutMapping("/nicknameChange/{id}")
    public Object nicknameUpdate(@PathVariable Long id, @RequestParam("nickname") String nickname) {
        Map<String, Object> map = new HashMap<>();
        String result = "실패, 다시 시도해주세요";
        switch (memberService.updateNickname(id, nickname)) {
            case "duplicate":
                result = "중복된 닉네임 입니다.";
                break;

            case "ok":
                result = "닉네임 수정 완료되었습니다.";
                break;
        }
        map.put("result", result);
        return map;
    }


    @PutMapping("/phoneChange/{id}")
    public Object telUpdate(@PathVariable Long id, @RequestParam("tel") String tel) {
        Map<String, Object> map = new HashMap<>();
        String result = "실패, 다시 시도해주세요";
        switch (memberService.updateTel(id, tel)) {
            case "duplicate":
                result = "중복된 번호 입니다.";
                break;

            case "ok":
                result = "번호 수정 완료되었습니다.";
                break;
        }
        map.put("result", result);
        return map;
    }

    @DeleteMapping("/member/delete/{id}")
    public Long memeberDelete(@PathVariable Long id) {
        return memberService.deleteMember(id);
    }


}
