package com.testsystem.service;

import com.testsystem.entity.ExamRegistration;

import java.util.List;

public interface ExamRegistrationService {

    ExamRegistration findByExamIdAndUserId(Long examId, Long userId);

    List<ExamRegistration> findByUserId(Long userId);

    void register(Long examId, Long userId);
}
