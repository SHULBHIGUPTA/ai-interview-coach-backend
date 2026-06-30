package com.example.interviewcoach.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private String category;
    private String question;
    private String answer;
}
