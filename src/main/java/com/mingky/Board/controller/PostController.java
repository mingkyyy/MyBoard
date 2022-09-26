package com.mingky.Board.controller;

import com.google.gson.JsonObject;
import com.mingky.Board.domain.Member;
import com.mingky.Board.domain.Post;
import com.mingky.Board.domain.Report;
import com.mingky.Board.dto.FreeUpdateDto;
import com.mingky.Board.dto.FreeWriteDto;
import com.mingky.Board.repository.PostRepository;
import com.mingky.Board.repository.ReportRepository;
import com.mingky.Board.service.PostService;
import com.mingky.Board.util.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    @PostMapping("/free/write")
    public Long freeSave(@RequestBody FreeWriteDto freeWriteDto, @CurrentMember Member member) {
        freeWriteDto.setMember(member);
        return postService.freeSave(freeWriteDto);
    }

    @PostMapping("/board/like")
    public Object boardLike(@RequestParam("id") Long id, @CurrentMember Member member) {
        Map<String, Object> map = new HashMap<>();
        int likeCount = postRepository.findById(id).orElseThrow().getLikers().size();
        String result = "실패";
        switch (postService.boardLike(member, id)) {
            case "addLike":
                result = "좋아요 성공";
                likeCount += 1;
                break;
            case "deleteLike":
                result = "좋아요 취소";
                likeCount -= 1;
        }

        map.put("result", result);
        map.put("likeCount", likeCount);

        return map;
    }

    @PutMapping("/board/free/read/{id}")
    public Long freeUpdate(@RequestBody FreeUpdateDto freeUpdateDto, @PathVariable Long id) {
        return postService.update(freeUpdateDto, id);
    }

    @DeleteMapping("/board/free/read/{id}")
    public Long boardDelete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }

    @PostMapping(value = "/uploadSummernoteImageFile", produces = "application/json")
    public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "D:\\summernote_image\\";    //저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();    //오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자

        String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);    //파일 저장
            jsonObject.addProperty("url", "/summernoteImage/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);    //저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }


}
