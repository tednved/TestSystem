package com.testsystem.controller;

import com.testsystem.common.ApiResponse;
import com.testsystem.dto.RegisterExamRequest;
import com.testsystem.entity.Exam;
import com.testsystem.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * 考试列表：
     * - 不带 userId：用于首页右侧 “考试信息” 区块
     * - 带 userId：用于考试列表页，附带 registered 字段
     */
    @GetMapping("/exams")
    public ApiResponse<List<Exam>> listExams(
            @RequestParam(value = "userId", required = false) Long userId) {
        List<Exam> list = examService.listExams(userId);
        return ApiResponse.success(list);
    }

    /**
     * 报名接口：
     * POST /api/exams/{id}/register
     * body: { "userId": 1 }
     */
    @PostMapping("/exams/{id}/register")
    public ApiResponse<Void> register(
            @PathVariable("id") Long examId,
            @RequestBody RegisterExamRequest request) {

        if (request == null || request.getUserId() == null) {
            return ApiResponse.error(1, "userId 不能为空");
        }

        try {
            examService.registerExam(examId, request.getUserId());
            return ApiResponse.success(null);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ApiResponse.error(1, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "报名失败，请稍后重试");
        }
    }
}
