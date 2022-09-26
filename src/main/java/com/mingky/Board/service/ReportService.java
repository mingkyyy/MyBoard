package com.mingky.Board.service;

import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import com.mingky.Board.domain.Report;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    public Long saveReport(Long id, Member member, String reportText) {
        Post post = postRepository.findById(id).orElseThrow();

        Report report = Report.builder()
                .reportText(reportText)
                .reportMember(member)
                .reportPost(post)
                .build();
        reportRepository.save(report);

        return id;
    }

    public Long deleteReport(Long id) {
        Report report = reportRepository.findById(id).orElseThrow();
        reportRepository.delete(report);
        return id;
    }
}
