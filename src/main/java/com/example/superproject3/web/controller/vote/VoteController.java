package com.example.superproject3.web.controller.vote;

import com.example.superproject3.service.post.VoteService;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.web.FindUserByToken;
import com.example.superproject3.web.dto.post.PostResponse;
import com.example.superproject3.web.dto.post.UserVoteRequest;
import com.example.superproject3.web.dto.post.VoteRequest;
import com.example.superproject3.web.dto.post.VoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final FindUserByToken findUserByToken;

    // 투표하기
    @Operation(summary = "투표하기 (API 번호: 18번)", description = "투표하기")
    @PostMapping
    public ResponseEntity postUserVote(@AuthenticationPrincipal CustomUserDetails customUserDetails, UserVoteRequest userVoteRequest){
        voteService.addVote(findUserByToken.findUser(customUserDetails), userVoteRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 투표 취소하기
    @Operation(summary = "투표 취소하기 (API 번호: 18번)", description = "투표 취소하기")
    @DeleteMapping
    public ResponseEntity deleteUserVote(@AuthenticationPrincipal CustomUserDetails customUserDetails, UserVoteRequest userVoteRequest){
        voteService.cancelVote(findUserByToken.findUser(customUserDetails), userVoteRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
