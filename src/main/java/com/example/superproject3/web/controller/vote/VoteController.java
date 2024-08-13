package com.example.superproject3.web.controller.vote;

import com.example.superproject3.service.vote.VoteService;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.web.FindUserByToken;
import com.example.superproject3.web.dto.post.PostResponse;
import com.example.superproject3.web.dto.vote.UserVoteRequest;
import com.example.superproject3.web.dto.vote.UserVoteResponse;
import com.example.superproject3.web.dto.vote.VoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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

    // 사용자가 투표한 항목 확인
    @Operation(summary = "사용자 투표여부 확인 (API 번호: 18번)", description = "사용자가 투표를 했는지 확인한다.")
    @GetMapping("/{vote-id}")
    public ResponseEntity<UserVoteResponse> getUserVote(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @Parameter(name = "vote-id", description = "투표 ID", required = true, example = "1") @PathVariable("vote-id") Long voteId) {
        UserVoteResponse response = voteService.getUserVote(findUserByToken.findUser(customUserDetails), voteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 투표하기
    @Operation(summary = "투표하기 (API 번호: 18번)", description = "투표하기")
    @ApiResponse(
            responseCode = "204",
            description = "투표하기, 응답값 없음"
    )
    @PostMapping
    public ResponseEntity postUserVote(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @RequestBody UserVoteRequest userVoteRequest) {
        voteService.addVote(findUserByToken.findUser(customUserDetails), userVoteRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 투표 취소하기
    @Operation(summary = "투표 취소하기 (API 번호: 18번)", description = "투표 취소하기")
    @ApiResponse(
            responseCode = "204",
            description = "투표 취소하기, 응답값 없음"
    )
    @DeleteMapping
    public ResponseEntity deleteUserVote(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestBody UserVoteRequest userVoteRequest) {
        voteService.cancelVote(findUserByToken.findUser(customUserDetails), userVoteRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
