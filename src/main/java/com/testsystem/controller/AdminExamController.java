package com.testsystem.controller;

import com.testsystem.common.ApiResponse;
import com.testsystem.entity.Exam;
import com.testsystem.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exams")
public class AdminExamController {

    private final ExamService examService;

    public AdminExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * 管理端：考试列表
     */
    @GetMapping
    public ApiResponse<List<Exam>> list() {
        List<Exam> list = examService.listAllForAdmin();
        return ApiResponse.success(list);
    }

    /**
     * 管理端：单个考试详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Exam> detail(@PathVariable("id") Long id) {
        Exam exam = examService.getById(id);
        if (exam == null) {
            return ApiResponse.error(1, "考试不存在");
        }
        return ApiResponse.success(exam);
    }

    /**
     * 管理端：更新考试
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable("id") Long id,
                                    @RequestBody Exam exam) {
        exam.setId(id);
        try {
            examService.updateExam(exam);
            return ApiResponse.success(null);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ApiResponse.error(1, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "更新失败，请稍后重试");
        }
    }

    /**
     * 管理端：删除考试
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        try {
            examService.deleteExam(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(500, "删除失败，请稍后重试");
        }
    }
}
