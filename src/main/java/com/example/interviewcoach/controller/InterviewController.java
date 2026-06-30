package com.example.interviewcoach.controller;

import com.example.interviewcoach.dto.AnswerRequest;
import com.example.interviewcoach.dto.QuestionRequest;
import com.example.interviewcoach.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewService interviewService;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @GetMapping("/debug-env")
    public ResponseEntity<?> debugEnv() {
        String envVar = System.getenv("SPRING_DATA_MONGODB_URI");
        return ResponseEntity.ok(Map.of(
            "env_var_set", envVar != null ? "YES" : "NO",
            "env_var_prefix", envVar != null ? envVar.substring(0, Math.min(30, envVar.length())) : "null",
            "spring_uri_prefix", mongoUri != null ? mongoUri.substring(0, Math.min(30, mongoUri.length())) : "null"
        ));
    }

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
