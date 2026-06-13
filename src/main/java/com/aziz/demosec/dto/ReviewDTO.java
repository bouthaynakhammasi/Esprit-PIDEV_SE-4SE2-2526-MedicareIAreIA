package com.aziz.demosec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String patientName;
    private String initials;
    private Integer rating;
    private String comment;
    private String createdAt;
}
