package com.mingky.Board.dto;

import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.MemberType;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Data
@NoArgsConstructor
public class SignupDto {


    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Length(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다.")
    private String name;


    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식을 지켜주세요. (test@test.com))")
    private String email;



    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Length(min=2, max=10, message="닉네임은 2자 이상 10자 이하여야 합니다.")
    private String nickname;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,}$",
            message = "패스워드는 영문자, 숫자, 특수기호를 조합하여 최소 8자 이상을 입력하셔야 합니다."
    )
    private String password;

    private String passwordVerify;


    @Pattern(
            regexp = "^(?=.*[0-9]).{10,11}$",
            message = "개인 휴대전화는 \'-\' 없이 숫자만 입력해야 합니다."
    )
    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    private String tel;




    @Builder
    public SignupDto(String name, String email, String nickname, String password, String tel){
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.tel = tel;
    }


    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .password(password)
                .tel(tel)
                .memberType(MemberType.ROLE_MEMBER)
                .build();
    }


}
