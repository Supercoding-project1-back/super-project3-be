package com.example.superproject3.service.mapper;

import com.example.superproject3.repository.vote.Vote;
import com.example.superproject3.web.dto.vote.UserVoteResponse;
import com.example.superproject3.web.dto.vote.VoteRequest;
import com.example.superproject3.web.dto.vote.VoteResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    Vote toEntity(VoteRequest voteRequest);

    VoteResponse toResponse(Vote vote);
}
