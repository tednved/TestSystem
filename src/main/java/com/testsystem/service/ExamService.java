package com.testsystem.service;

import com.testsystem.entity.Exam;

import java.util.List;

public interface ExamService {

    // 前台考试列表（userId 可为 null）
    List<Exam> listExams(Long userId);

    // 报名
    void registerExam(Long examId, Long userId);

    // 管理端：列表
    List<Exam> listAllForAdmin();

    // 管理端：详情
    Exam getById(Long id);

    // 管理端：更新
    void updateExam(Exam exam);

    // 管理端：删除
    void deleteExam(Long id);
}
