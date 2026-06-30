package com.example.interviewcoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterviewcoachApplication {

	public static void main(String[] args) {
		String mongoUri = System.getenv("SPRING_DATA_MONGODB_URI");
		if (mongoUri == null || mongoUri.isBlank()) {
			System.err.println("FATAL: SPRING_DATA_MONGODB_URI environment variable is not set!");
			System.exit(1);
		}
		String geminiKey = System.getenv("GEMINI_API_KEY");
		if (geminiKey == null || geminiKey.isBlank()) {
			System.err.println("FATAL: GEMINI_API_KEY environment variable is not set!");
			System.exit(1);
		}
		System.out.println("INFO: MongoDB URI prefix = " + mongoUri.substring(0, Math.min(30, mongoUri.length())) + "...");
		SpringApplication.run(InterviewcoachApplication.class, args);
	}

}
