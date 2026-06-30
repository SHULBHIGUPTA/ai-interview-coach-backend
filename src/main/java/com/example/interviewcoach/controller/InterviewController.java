package com.example.interviewcoach.controller;

import com.example.interviewcoach.dto.AnswerRequest;
import com.example.interviewcoach.dto.QuestionRequest;
import com.example.interviewcoach.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/generate-question")
    public ResponseEntity<?> generateQuestion(
            @RequestBody QuestionRequest request) {

        return ResponseEntity.ok(
                interviewService.generateQuestion(request));
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeAnswer(
            @RequestBody AnswerRequest request) {

        return ResponseEntity.ok(
                interviewService.analyzeAnswer(request));
    }

    @GetMapping("/history")
    public ResponseEntity<?> history() {
        return ResponseEntity.ok(interviewService.getHistory());
    }
}
