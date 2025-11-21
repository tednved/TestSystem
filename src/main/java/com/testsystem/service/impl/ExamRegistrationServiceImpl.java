package com.testsystem.service.impl;

import com.testsystem.entity.Exam;
import com.testsystem.entity.ExamRegistration;
import com.testsystem.entity.User;
import com.testsystem.mapper.ExamMapper;
import com.testsystem.mapper.ExamRegistrationMapper;
import com.testsystem.mapper.UserMapper;
import com.testsystem.service.ExamRegistrationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamRegistrationServiceImpl implements ExamRegistrationService {

    private final ExamRegistrationMapper registrationMapper;
    private final ExamMapper examMapper;
    private final UserMapper userMapper;

    public ExamRegistrationServiceImpl(ExamRegistrationMapper registrationMapper,
                                       ExamMapper examMapper,
                                       UserMapper userMapper) {
        this.registrationMapper = registrationMapper;
        this.examMapper = examMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ExamRegistration findByExamIdAndUserId(Long examId, Long userId) {
        return registrationMapper.findByExamIdAndUserId(examId, userId);
    }

    @Override
    public List<ExamRegistration> findByUserId(Long userId) {
        return registrationMapper.findByUserId(userId);
    }

    @Override
    public void register(Long examId, Long userId) {
        // 校验用户
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if ("admin".equals(user.getRole())) {
            throw new IllegalStateException("管理员账号禁止报名考试");
        }

        // 校验考试 & 时间窗口
        Exam exam = examMapper.findById(examId);
        if (exam == null) {
            throw new IllegalArgumentException("考试不存在");
        }

        LocalDateTime now = LocalDateTime.now();

        if (exam.getExamEndTime() != null && now.isAfter(exam.getExamEndTime())) {
            throw new IllegalStateException("考试已结束");
        }
        if (exam.getSignupStartTime() != null && now.isBefore(exam.getSignupStartTime())) {
            throw new IllegalStateException("报名尚未开始");
        }
        if (exam.getSignupEndTime() != null && now.isAfter(exam.getSignupEndTime())) {
            throw new IllegalStateException("报名已结束");
        }

        // 查是否已存在报名
        ExamRegistration existing = registrationMapper.findByExamIdAndUserId(examId, userId);
        if (existing != null && "applied".equals(existing.getStatus())) {
            // 已报名，直接返回即可
            return;
        }

        if (existing != null) {
            // 之前取消过，重新激活
            registrationMapper.updateStatus(existing.getId(), "applied");
        } else {
            // 新报名
            ExamRegistration reg = new ExamRegistration();
            reg.setExamId(examId);
            reg.setUserId(userId);
            reg.setStatus("applied");
            reg.setRegisteredAt(LocalDateTime.now());
            registrationMapper.insert(reg);
        }
    }
}
