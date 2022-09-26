package com.mingky.Board.util;

import com.mingky.Board.domain.Member;
import com.mingky.Board.dto.SignupDto;
import com.mingky.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignupDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupDto signupDto = (SignupDto) target;
        Optional<Member> optional = memberRepository.findByEmail(signupDto.getEmail());
        Optional<Member> nickname = memberRepository.findByNickname(signupDto.getNickname());
        Optional<Member> tel = memberRepository.findByTel(signupDto.getTel());

        if (optional.isPresent()) {
            errors.rejectValue(
                    "email",
                    "duplicate.email",
                    "이미 가입된 이메일입니다."
            );
        } else if (nickname.isPresent()) {
            errors.rejectValue("nickname",
                    "duplicate.nickname",
                    "중복된 닉네임 입니다."
            );
        } else if (tel.isPresent()) {
            errors.rejectValue(
                    "tel",
                    "duplicate.tel",
                    "중복된 핸드폰 번호 입니다."
            );
        }else if (!(signupDto).getPassword().equals((signupDto).getPasswordVerify())) {
            errors.rejectValue("passwordVerify", "password.verify.failed", "비밀번호가 일치하지 않습니다.");
        }
    }

}
