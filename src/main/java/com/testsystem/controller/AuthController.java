package com.testsystem.controller;

import com.testsystem.common.ApiResponse;
import com.testsystem.dto.LoginRequest;
import com.testsystem.dto.LoginResponse;
import com.testsystem.dto.RegisterRequest;
import com.testsystem.entity.User;
import com.testsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // 构造注入
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());

        if (user == null) {
            return ApiResponse.error(4001, "用户名或密码错误");
        }

        LoginResponse resp = new LoginResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole());
        resp.setRealName(user.getRealName());
        resp.setGender(user.getGender());
        resp.setUniversity(user.getUniversity());
        resp.setGrade(user.getGrade());

        return ApiResponse.success(resp);
    }

    /**
     * 注册接口
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest request) {

        // 1. 简单校验：用户名是否已存在
        User exists = userService.findByUsername(request.getUsername());
        if (exists != null) {
            return ApiResponse.error(4002, "用户名已存在");
        }

        // 2. 构造 User 实体
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole("user");  // 注册默认角色为普通用户

        user.setRealName(request.getRealName());
        user.setIdCard(request.getIdCard());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setUniversity(request.getUniversity());
        user.setGrade(request.getGrade());

        // 3. 保存
        userService.register(user);

        return ApiResponse.success(null);
    }
}
