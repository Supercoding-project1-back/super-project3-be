package com.example.superproject3.service.mapper;

import com.example.superproject3.repository.post.Vote;
import com.example.superproject3.web.dto.post.VoteRequest;
import com.example.superproject3.web.dto.post.VoteResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    Vote toEntity(VoteRequest voteRequest);

    VoteResponse toResponse(Vote vote);
}
