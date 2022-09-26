package com.mingky.Board.controller;

import com.mingky.Board.domain.*;
import com.mingky.Board.dto.SignupDto;
import com.mingky.Board.repository.CommentRepository;
import com.mingky.Board.repository.MemberRepository;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.repository.ReportRepository;
import com.mingky.Board.service.MemberService;
import com.mingky.Board.service.PostService;
import com.mingky.Board.util.CurrentMember;
import com.mingky.Board.util.JoinValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @PostConstruct
   @Transactional
    public void createPost(){
        Member member = Member.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test12345!"))
                .tel("0100000")
                .nickname("테스트")
                .memberType(MemberType.ROLE_MANAGE)
                .build();

        memberRepository.save(member);

        for (int i =0; i<30; i++) {
            Post post = Post.builder()
                    .title((i+1)+"번째 제목")
                    .content((i+1)+"번째 내용")
                    .write(memberRepository.getById(1l))
                    .category(Category.FREE)
                    .build();

           postRepository.save(post);
        }

        for (int i =0; i<10; i++){
            Report report = Report.builder()
                    .reportMember(member)
                    .reportText(i+"번째 신고")
                    .reportPost(postRepository.getById(1l))
                    .build();

            reportRepository.save(report);
        }
   }

    @InitBinder("signupDto")
    protected void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(new JoinValidator(memberRepository));
    }

    @GetMapping("/")
    public String index(Model model, @CurrentMember Member member){
        model.addAttribute("member", member);
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupDto signupDto, Errors errors){
        if (errors.hasErrors()) {
            return "/member/signup";
        }
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        memberService.save(signupDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }


    @GetMapping("/board/free")
    public String freeBoard(@CurrentMember Member member, Model model,
                            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        Page<Post> freeList = postService.findCategoryPage(Category.FREE, pageable);
        model.addAttribute("freeList", freeList);
        model.addAttribute("pageable", pageable);
        model.addAttribute("member", member);

        int startPage = Math.max(1, freeList.getPageable().getPageNumber() - 5);
        int endPage = Math.min(freeList.getTotalPages(), freeList.getPageable().getPageNumber() + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/free/list";

    }

    @GetMapping("/board/free/read/{id}")
    public String freeRead(Model model, @PathVariable Long id, @CurrentMember Member member, HttpServletRequest request, HttpServletResponse response){
        Post post = postService.findRead(id);
        postService.CookieHit(id,request,response);
        List<Comment> commentList = commentRepository.findByPost(post);
        Collections.reverse(commentList);
        model.addAttribute("post",post);
        model.addAttribute("member", member);
        model.addAttribute("commentList",commentList);
        return "/board/free/read";
    }


    @GetMapping("/board/free/write")
    public String freeWrite(@CurrentMember Member member){
        return "/board/free/write";
    }

    @GetMapping("/board/free/update/{id}")
    public String freeUpdate(Model model,@CurrentMember Member member, @PathVariable Long id){
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "/board/free/update";
    }

    @GetMapping("/mypage/{email}")
    public String mypage(Model model, @CurrentMember Member member,
                         @PathVariable String email){

        if (member == null || !member.getEmail().equals(email)){
            return "redirect:/";
        }

        member = memberRepository.findByEmail(email).orElseThrow();
        model.addAttribute("member", member);
        return "member/mypage";
    }

    @GetMapping("/report/{id}")
    public String report(Model model, @PathVariable Long id){
        Post post = postRepository.findById(id).orElseThrow();

        model.addAttribute("post", post);
        return "/member/report";
    }

    @GetMapping("/manage")
    public String managePage(Model model, @CurrentMember Member member,
                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        if (!member.getMemberType().toString().equals("ROLE_MANAGE")){
            return "redirect:/";
        }
        Page<Report> reportList = reportRepository.findAll(pageable);
        int startPage = Math.max(1, reportList.getPageable().getPageNumber() - 5);
        int endPage = Math.min(reportList.getTotalPages(), reportList.getPageable().getPageNumber() + 5);

        model.addAttribute("pageable", pageable);
        model.addAttribute("reportList", reportList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/manage/list";
    }

    @GetMapping("/report/read/{id}")
    public String manageRead(Model model, @PathVariable Long id){
        Report report = reportRepository.findById(id).orElseThrow();
        model.addAttribute("report", report);

        return "/manage/reportList";
    }

}