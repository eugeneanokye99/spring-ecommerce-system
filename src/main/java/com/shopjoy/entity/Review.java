package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    private int reviewId;
    private int productId;
    private int userId;
    private int rating;
    private String title;
    private String comment;
    private boolean isVerifiedPurchase;
    private int helpfulCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
