package com.testsystem.controller;

import com.testsystem.common.ApiResponse;
import com.testsystem.common.PageResult;
import com.testsystem.entity.User;
import com.testsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表
     * GET /api/admin/users?page=1&size=10
     */
    @GetMapping
    public ApiResponse<PageResult<User>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResult<User> result = userService.pageUsers(page, size);
        return ApiResponse.success(result);
    }

    /**
     * 删除用户
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}
