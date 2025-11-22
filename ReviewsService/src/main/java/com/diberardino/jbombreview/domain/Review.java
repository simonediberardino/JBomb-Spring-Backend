package com.diberardino.jbombreview.domain;

import com.diberardino.jbombreview.data.ReviewEntity;

public record Review(
        Long id,
        String author,
        String content,
        Integer rating,
        String createdAt
) {
    public ReviewEntity toEntity() {
        return new ReviewEntity(author, content, rating);
    }
}