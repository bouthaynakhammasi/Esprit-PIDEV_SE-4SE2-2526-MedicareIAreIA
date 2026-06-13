package com.aziz.demosec.service;

import com.aziz.demosec.Entities.Review; // Adjust if your Review entity is in another package
import com.aziz.demosec.dto.ReviewDTO;
import com.aziz.demosec.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDTO> getDoctorReviews(Long doctorId) {
        log.info("[ReviewService] fetching reviews for doctorId: {}", doctorId);
        List<Review> reviews = reviewRepository.findByDoctorIdWithPatient(doctorId);
        log.info("[ReviewService] found {} reviews", reviews.size());
        
        return reviews.stream().map(r -> {
            String name = (r.isAnonymous() || r.getPatient() == null) 
                ? "Anonymous" 
                : r.getPatient().getFullName();
            
            String initials = "Anonymous".equals(name) ? "?" 
                : Arrays.stream(name.split(" "))
                    .filter(w -> !w.isEmpty())
                    .map(w -> String.valueOf(w.charAt(0)).toUpperCase())
                    .collect(Collectors.joining());
                    
            String date = r.getCreatedAt() != null 
                ? r.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) 
                : "";
                
            return new ReviewDTO(r.getId(), name, initials, r.getRating(), r.getComment(), date);
        }).collect(Collectors.toList());
    }

    public java.util.Map<String, Object> getDoctorReviewStats(Long doctorId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        try {
            // 1. Get Total Count and Average Rating
            // Note: Spring Data JPA might return Object[] directly, or a List<Object[]> depending on internal mapping.
            // But since we declared Object[], it expects a single row.
            // If there are no reviews, COUNT returns 0, AVG returns null.
            Object[] data = reviewRepository.getReviewStats(doctorId);
            if (data != null && data.length == 2) {
                Long totalReviews = ((Number) data[0]).longValue();
                Double averageRating = data[1] != null ? ((Number) data[1]).doubleValue() : 0.0;
                
                stats.put("totalReviews", totalReviews);
                stats.put("averageRating", averageRating);
            } else {
                stats.put("totalReviews", 0L);
                stats.put("averageRating", 0.0);
            }

            // 2. Get Rating Breakdown (Distribution)
            List<Object[]> breakdown = reviewRepository.getRatingBreakdown(doctorId);
            java.util.Map<Integer, Long> ratingDistribution = new java.util.HashMap<>();
            // Initialize 1-5 stars with 0
            for (int i = 1; i <= 5; i++) {
                ratingDistribution.put(i, 0L);
            }

            // Populate with actual data
            for (Object[] row : breakdown) {
                if (row != null && row.length == 2) {
                    Integer rating = ((Number) row[0]).intValue();
                    Long count = ((Number) row[1]).longValue();
                    ratingDistribution.put(rating, count);
                }
            }
            stats.put("ratingDistribution", ratingDistribution);

        } catch (Exception e) {
            log.error("Error calculating review stats for doctor {}: {}", doctorId, e.getMessage());
            stats.put("totalReviews", 0L);
            stats.put("averageRating", 0.0);
            stats.put("ratingDistribution", new java.util.HashMap<>());
        }

        return stats;
    }
}
