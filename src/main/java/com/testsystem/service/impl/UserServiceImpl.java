package com.testsystem.service.impl;

import com.testsystem.common.PageResult;
import com.testsystem.entity.User;
import com.testsystem.mapper.UserMapper;
import com.testsystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    // 构造注入
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!Objects.equals(user.getPassword(), password)) {
            return null;
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public void register(User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }
        userMapper.insert(user);
    }

    // ===== 新增：后台用户管理 =====

    @Override
    public PageResult<User> pageUsers(int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;

        int offset = (page - 1) * size;
        long total = userMapper.countAll();
        List<User> records = userMapper.findPage(offset, size);

        return new PageResult<>(total, records);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
}
