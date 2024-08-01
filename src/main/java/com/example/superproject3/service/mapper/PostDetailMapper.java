package com.example.superproject3.service.mapper;

import com.example.superproject3.repository.post.PostDetail;
import com.example.superproject3.web.dto.PostDetailRequest;
import com.example.superproject3.web.dto.PostDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostDetailMapper {
    @Mapping(target = "image1", ignore = true)
    @Mapping(target = "image2", ignore = true)
    PostDetail toEntity(PostDetailRequest postDetailRequest);

    PostDetailResponse toResponse(PostDetail postDetail);
}
