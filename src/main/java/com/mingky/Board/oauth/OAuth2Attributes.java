package com.mingky.Board.oauth;

import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;


    public static OAuth2Attributes of(String registeredId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registeredId)) {
            return ofNaver("id", attributes);
        }
        if ("kakao".equals(registeredId)){
            return ofKakao("id",attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }


    private static OAuth2Attributes ofNaver(String id, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(id)
                .build();

    }

    private static OAuth2Attributes ofGoogle(String id, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(id)
                .build();
    }

    private static OAuth2Attributes ofKakao(String id, Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) kakaoProfile.get("nickname"))
                .attributes(attributes)
                .nameAttributeKey(id)
                .build();
    }


    public Member toMember(){
        return Member.builder()
                .email(email)
                .memberType(MemberType.ROLE_MEMBER)
                .password("{noop}")
                .nickname(name)
                .tel("{noop}")
                .name(name)
                .build();
    }
}
