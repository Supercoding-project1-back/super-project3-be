package com.example.superproject3.service.vote;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.vote.Vote;
import com.example.superproject3.repository.vote.VoteRepository;
import com.example.superproject3.repository.userPost.UserVote;
import com.example.superproject3.repository.userPost.UserVoteRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.service.mapper.VoteMapper;
import com.example.superproject3.web.dto.vote.UserVoteRequest;
import com.example.superproject3.web.dto.vote.UserVoteResponse;
import com.example.superproject3.web.dto.vote.VoteRequest;
import com.example.superproject3.web.dto.vote.VoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteMapper voteMapper;
    private final VoteRepository voteRepository;
    private final UserVoteRepository userVoteRepository;

    // 게시글 세부사항 생성
    public Vote createVote(VoteRequest voteRequest) {
        Vote vote = voteMapper.toEntity(voteRequest);
        return voteRepository.save(vote);
    }

    public VoteResponse getVote(Vote vote) {
        return voteMapper.toResponse(vote);
    }

    // 투표 수정하기
    public void modifyVote(Vote vote, VoteRequest voteRequest) {
        if (voteRequest.getItem1() != null) vote.setItem1(voteRequest.getItem1());
        if (voteRequest.getItem2() != null) vote.setItem2(voteRequest.getItem2());
        if (voteRequest.getItem3() != null) vote.setItem3(voteRequest.getItem3());
        if (voteRequest.getItem4() != null) vote.setItem4(voteRequest.getItem4());

        voteRepository.save(vote);
    }

    // 사용자의 투표 여부 확인하기
    public UserVoteResponse getUserVote(User user, long voteId) {
        Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new IllegalArgumentException("투표가 없습니다."));
        UserVote userVote = userVoteRepository.findByUserAndVote(user, vote).orElse(null);
        if (userVote == null) {
            return UserVoteResponse.builder()
                    .id(vote.getId())
                    .vote_item(null)
                    .build();
        }

        return UserVoteResponse.builder()
                .id(vote.getId())
                .vote_item(userVote.getVote_item())
                .build();
    }

    // 투표 삭제하기
    public void deleteVote(Post post) {
        voteRepository.delete(post.getVote());
    }

    // 투표 추가
    public void addVote(User user, UserVoteRequest voteRequest) {
        Vote vote = voteRepository.findById(voteRequest.getVote_id()).orElseThrow(() -> new IllegalArgumentException("투표가 없습니다."));

        // 투표여부 확인
        if (userVoteRepository.findByUserAndVote(user, vote).isPresent())
            throw new IllegalArgumentException("이미 투표했습니다.");

        // 투표항목 존재 여부 확인
        String voteItem = voteRequest.getVote_item();
        if (!voteItem.equals(vote.getItem1()) && !voteItem.equals(vote.getItem2()) && !voteItem.equals(vote.getItem3()) && !voteItem.equals(vote.getItem4()))
            throw new IllegalArgumentException("투표항목에 없는 것 요청함");

        UserVote userVote = UserVote.builder()
                .user(user)
                .vote(vote)
                .vote_item(voteItem)
                .build();
        userVoteRepository.save(userVote);
    }

    // 투표 취소
    public void cancelVote(User user, UserVoteRequest voteRequest) {
        Vote vote = voteRepository.findById(voteRequest.getVote_id()).orElseThrow(() -> new IllegalArgumentException("투표가 없습니다."));

        // 투표항목 존재 여부 확인
        String voteItem = voteRequest.getVote_item();
        if (!voteItem.equals(vote.getItem1()) && !voteItem.equals(vote.getItem2()) && !voteItem.equals(vote.getItem3()) && !voteItem.equals(vote.getItem4()))
            throw new IllegalArgumentException("투표항목에 없는 것 요청함");

        UserVote userVote = userVoteRepository.findByUserAndVote(user, vote)
                .orElseThrow(() -> new IllegalArgumentException("투표한 적이 없습니다."));

        userVoteRepository.delete(userVote);
    }
}
