package com.example.interviewcoach.service;

import com.example.interviewcoach.dto.AnswerRequest;
import com.example.interviewcoach.dto.QuestionRequest;
import com.example.interviewcoach.model.Interview;
import com.example.interviewcoach.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    private static final String GEMINI_BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/";

    @Autowired
    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
        this.restTemplate = new RestTemplate();
    }

    public String generateQuestion(QuestionRequest request) {
        String prompt = "Generate a technical interview question for the category: " + request.getCategory();
        String question = callGemini(prompt);

        Interview interview = new Interview();
        interview.setCategory(request.getCategory());
        interview.setQuestion(question);
        interviewRepository.save(interview);

        return question;
    }

    public String analyzeAnswer(AnswerRequest request) {
        String prompt = "You are an interview coach. The interview question was: \"" + request.getQuestion()
                + "\". The candidate answered: \"" + request.getAnswer()
                + "\". Provide constructive feedback and a score out of 10.";

        String feedback = callGemini(prompt);

        Interview interview = new Interview();
        interview.setCategory(request.getCategory());
        interview.setQuestion(request.getQuestion());
        interview.setAnswer(request.getAnswer());
        interview.setFeedback(feedback);
        interviewRepository.save(interview);

        return feedback;
    }

    public List<Interview> getHistory() {
        return interviewRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    private String callGemini(String prompt) {
        String url = GEMINI_BASE_URL + geminiModel + ":generateContent?key=" + geminiApiKey;

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getBody() != null) {
                List<Map<String, Object>> candidates =
                        (List<Map<String, Object>>) response.getBody().get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> contentObj = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentObj.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        return (String) parts.get(0).get("text");
                    }
                }
            }
            return "Unable to generate response.";
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Gemini API error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Gemini server error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage(), e);
        }
    }
}
