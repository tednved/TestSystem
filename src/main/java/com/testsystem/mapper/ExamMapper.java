package com.testsystem.mapper;

import com.testsystem.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamMapper {

    // 普通/首页用：只算 phase，不区分用户
    List<Exam> findAllWithPhase();

    // 带 userId：在列表里多一个 registered 字段
    List<Exam> findAllWithUser(@Param("userId") Long userId);

    Exam findById(@Param("id") Long id);

    int insert(Exam exam);

    int update(Exam exam);

    int deleteById(@Param("id") Long id);

    // 报名相关
    int countRegistration(@Param("examId") Long examId,
                          @Param("userId") Long userId);

    int insertRegistration(@Param("examId") Long examId,
                           @Param("userId") Long userId);

    // 如果以后想删考试时顺带删报名，可以再加：
    // int deleteRegistrationsByExamId(@Param("examId") Long examId);
}
