package com.testsystem.service;

import com.testsystem.common.PageResult;
import com.testsystem.entity.User;

public interface UserService {

    /**
     * 登录：校验用户名和密码
     * @return 登录成功返回 User；失败返回 null
     */
    User login(String username, String password);

    /**
     * 根据用户名查询
     */
    User findByUsername(String username);

    /**
     * 注册新用户（普通 user）
     */
    void register(User user);

    // ===== 新增：后台用户管理 =====

    /**
     * 分页查询用户列表
     */
    PageResult<User> pageUsers(int page, int size);

    /**
     * 删除用户
     */
    void deleteUser(Long id);
}
