package com.diberardino.jbombreview.data;

import com.diberardino.jbombreview.domain.Review;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    public ReviewEntity() {
    }

    public ReviewEntity(String author, String content, Integer rating) {
        this.author = author;
        this.content = content;
        this.rating = rating;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Review toDomain() {
        return new Review(
                getId(),
                getAuthor(),
                getContent(),
                getRating(),
                getCreatedAt()
        );
    }
}
