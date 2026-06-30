package com.example.interviewcoach.repository;

import com.example.interviewcoach.model.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {
}
