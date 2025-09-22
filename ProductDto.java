package com.app.product;

import com.app.product_review.ProductReviewDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,
        @Min(value = 1, message = "count is required min 1")
        @Max(value = 1000000, message = "count is required max 1000000")
        int count,
        @Min(value = 0, message = "price is required min 0.01")
        @Max(value = 1000000, message = "price is required max 1000000")
        float price,
        @Min(value = 0, message = "discount is required min 0")
        @Max(value = 100, message = "discount is required max 100")
        int discount,

        Long categoryId,
        String categoryName,

        String img,

        String status,
        String statusName,

        int score,

        int incomeCount,
        float incomeSum,

        List<ProductReviewDto> reviews

) {
}
