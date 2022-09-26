package com.mingky.Board.repository;

import com.mingky.Board.domain.Comment;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    Optional<Member> findByCommentWriter(Member member);


}
