package com.edutrack.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CourseRatingRequest {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Min rating is 1")
    @Max(value = 5, message = "Max rating is 5")
    private int rating;

    private String review;

    public int getRating() { return rating; }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() { return review; }
    public void setReview(String review) {
        this.review = review;
    }
}