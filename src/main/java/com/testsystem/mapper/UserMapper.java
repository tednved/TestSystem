package com.testsystem.mapper;

import com.testsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    User findByUsername(String username);

    int insert(User user);

    // ====== 新增：后台用户管理用 ======

    /**
     * 分页查询用户列表
     * @param offset 偏移量
     * @param limit  每页数量
     */
    List<User> findPage(@Param("offset") int offset,
                        @Param("limit") int limit);

    /**
     * 查询用户总数
     */
    long countAll();

    /**
     * 根据 id 删除用户
     */
    int deleteById(Long id);

    User findById(@Param("id") Long id);
}
