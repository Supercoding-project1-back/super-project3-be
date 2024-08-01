package com.example.superproject3.web.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostRequest {
    private String title;
    private String content;
    private String category;
}
