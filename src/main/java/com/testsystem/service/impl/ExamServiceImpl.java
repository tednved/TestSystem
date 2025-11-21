package com.testsystem.service.impl;

import com.testsystem.entity.Exam;
import com.testsystem.mapper.ExamMapper;
import com.testsystem.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;

    public ExamServiceImpl(ExamMapper examMapper) {
        this.examMapper = examMapper;
    }

    @Override
    public List<Exam> listExams(Long userId) {
        if (userId == null) {
            return examMapper.findAllWithPhase();
        } else {
            return examMapper.findAllWithUser(userId);
        }
    }

    @Override
    @Transactional
    public void registerExam(Long examId, Long userId) {
        Exam exam = examMapper.findById(examId);
        if (exam == null) {
            throw new IllegalStateException("考试不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        if (exam.getSignupStartTime() != null && now.isBefore(exam.getSignupStartTime())) {
            throw new IllegalStateException("报名尚未开始");
        }
        if (exam.getSignupEndTime() != null && now.isAfter(exam.getSignupEndTime())) {
            throw new IllegalStateException("报名已结束");
        }

        int count = examMapper.countRegistration(examId, userId);
        if (count > 0) {
            // 已报名就不再插入（前端会直接标记为已报名）
            return;
        }

        examMapper.insertRegistration(examId, userId);
    }

    @Override
    public List<Exam> listAllForAdmin() {
        // 管理端也希望看到 phase，这里直接复用
        return examMapper.findAllWithPhase();
    }

    @Override
    public Exam getById(Long id) {
        return examMapper.findById(id);
    }

    @Override
    public void updateExam(Exam exam) {
        if (exam.getId() == null) {
            throw new IllegalArgumentException("考试 id 不能为空");
        }
        int rows = examMapper.update(exam);
        if (rows == 0) {
            throw new IllegalStateException("考试不存在或已被删除");
        }
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {
        examMapper.deleteById(id);
        // 如需同时删除报名记录，可以：
        // examMapper.deleteRegistrationsByExamId(id);
    }
}
