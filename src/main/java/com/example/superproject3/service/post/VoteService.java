package com.example.superproject3.service.post;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.Vote;
import com.example.superproject3.repository.post.VoteRepository;
import com.example.superproject3.repository.userPost.UserVote;
import com.example.superproject3.repository.userPost.UserVoteRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.service.mapper.VoteMapper;
import com.example.superproject3.web.dto.post.UserVoteRequest;
import com.example.superproject3.web.dto.post.VoteRequest;
import com.example.superproject3.web.dto.post.VoteResponse;
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

    public void modifyVote(Vote vote, VoteRequest voteRequest) {
        if(voteRequest.getItem1() != null) vote.setItem1(voteRequest.getItem1());
        if(voteRequest.getItem2() != null) vote.setItem2(voteRequest.getItem2());
        if(voteRequest.getItem3() != null) vote.setItem3(voteRequest.getItem3());
        if(voteRequest.getItem4() != null) vote.setItem4(voteRequest.getItem4());

        voteRepository.save(vote);
    }

    public void deleteVote(Post post) {
        voteRepository.delete(post.getVote());
    }

    // 투표 추가
    public void addVote(User user, UserVoteRequest voteRequest) {
        Vote vote = voteRepository.findById(voteRequest.getVote_id()).orElseThrow(() -> new IllegalArgumentException("투표가 없습니다."));

        if(userVoteRepository.findByUserAndVote(user, vote).isPresent())
            throw new IllegalArgumentException("이미 투표했습니다.");

        UserVote userVote = UserVote.builder()
                .user(user)
                .vote(vote)
                .vote_item(voteRequest.getVote_item())
                .build();
        userVoteRepository.save(userVote);
    }

    // 투표 취소


    public void cancelVote(User user,UserVoteRequest voteRequest) {
        Vote vote = voteRepository.findById(voteRequest.getVote_id()).orElseThrow(() -> new IllegalArgumentException("투표가 없습니다."));

        UserVote userVote = userVoteRepository.findByUserAndVote(user, vote)
                        .orElseThrow(() -> new IllegalArgumentException("투표한 적이 없습니다."));

        userVoteRepository.delete(userVote);
    }
}
