package com.shopjoy.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for Review.
 */
public class ReviewResponse {
    
    private int reviewId;
    private int userId;
    private String userName;
    private int productId;
    private String productName;
    private int rating;
    private String title;
    private String comment;
    private LocalDateTime createdAt;
    
    public ReviewResponse() {
    }
    
    public ReviewResponse(int reviewId, int userId, String userName, int productId, 
                         String productName, int rating, String title, String comment, 
                         LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.userName = userName;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
