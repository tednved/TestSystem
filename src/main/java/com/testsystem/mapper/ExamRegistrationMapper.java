package com.testsystem.mapper;

import com.testsystem.entity.ExamRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamRegistrationMapper {

    ExamRegistration findByExamIdAndUserId(@Param("examId") Long examId,
                                           @Param("userId") Long userId);

    List<ExamRegistration> findByUserId(@Param("userId") Long userId);

    int insert(ExamRegistration registration);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
