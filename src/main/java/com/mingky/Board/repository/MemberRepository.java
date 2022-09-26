package com.mingky.Board.repository;

import com.mingky.Board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByTel(String tel);

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickname);

    boolean existsByTel(String tel);
}
