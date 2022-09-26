package com.mingky.Board.repository;

import com.mingky.Board.domain.Category;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findByCategory(Category free, Pageable pageable);

    Page<Post> findByWrite(Member member, Pageable pageable);
}
