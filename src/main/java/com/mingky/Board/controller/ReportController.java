package com.mingky.Board.controller;

import com.mingky.Board.domain.Member;
import com.mingky.Board.service.PostService;
import com.mingky.Board.service.ReportService;
import com.mingky.Board.util.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/report/{id}")
    public Long saveReport(@PathVariable Long id, @RequestParam("reportText") String reportText
            , @CurrentMember Member member){
        return reportService.saveReport(id, member, reportText);

    }

    @DeleteMapping("/report/{id}")
    public Long deleteReport(@PathVariable Long id){
        return reportService.deleteReport(id);
    }
}
