package com.example.superproject3.web.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponse {
    private Long id;
    private String email;
    private String title;
    private String content;
    private String category;
    private String create_at;
}