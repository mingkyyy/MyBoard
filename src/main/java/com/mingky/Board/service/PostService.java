package com.mingky.Board.service;

import com.mingky.Board.domain.Category;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import com.mingky.Board.domain.Report;
import com.mingky.Board.dto.FreeUpdateDto;
import com.mingky.Board.dto.FreeWriteDto;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public Long freeSave(FreeWriteDto freeWriteDto) {

        return postRepository.save(freeWriteDto.freeWrite()).getId();
    }


    public Page<Post> findCategoryPage(Category free, Pageable pageable) {
        return postRepository.findByCategory(free, pageable);
    }

    public Post findRead(long id) {
        return postRepository.findById(id).orElseThrow();
    }


    @Transactional
    public String boardLike(Member member, Long id) {
        member = memberRepository.findById(member.getId()).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        if (member.getLike().contains(post) == true) {
            member.getLike().remove(post);
            return "deleteLike";
        } else {
            member.addLike(post);
            return "addLike";
        }
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
    }


    @Transactional
    public Long update(FreeUpdateDto freeUpdateDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.update(freeUpdateDto.getTitle(), freeUpdateDto.getContent());
        postRepository.save(post);
        return id;
    }

    public  void CookieHit(Long id, HttpServletRequest request, HttpServletResponse response){
        Post post = postRepository.findById(id).orElseThrow();
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("postView")){
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null){
            if (!oldCookie.getValue().contains("["+id.toString()+"]")){
                post.hitAdd(post.getHit()+1);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        }else {
            post.hitAdd(post.getHit()+1);
            Cookie newCookie = new Cookie("postView","[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
        postRepository.save(post);
    }


    public Page<Post> findWritePage(Member member, Pageable pageable) {
        return postRepository.findByWrite(member, pageable);

    }


}