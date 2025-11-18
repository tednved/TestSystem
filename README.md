# 考试报名与管理系统（后端）

本项目是一个基于 **Spring Boot + MyBatis + MySQL** 的考试报名与管理系统后端，当前已实现：

- 用户注册与登录
- 按角色区分普通用户（user）与管理员（admin）
- 管理员端用户管理（分页查询 + 删除）
- 与 Vue3 前端通过 JSON 接口联调

前端项目通过 Vite 代理访问本后端，日常开发环境下的端口为：

- 后端：`http://localhost:8081`
- 前端：`http://localhost:5173`（通过 `/api` 代理到后端）

---

## 一、技术栈

- Java 17
- Spring Boot 3.5.7
- Spring Web
- Spring JDBC
- MyBatis / MyBatis-Spring-Boot-Starter
- MySQL 9.x
- HikariCP 连接池
- Maven 构建

---

## 二、数据库设计

### 1. 数据库基本信息

- 数据库名：`testsystem_database`
- 主要表：
  - `user`：用户信息表（账号信息 + 个人信息）
  - `exam`：考试信息表（预留，后续扩展）
  - `exam_registration`：考试报名表（预留，后续扩展）

### 2. user 表结构

```sql
CREATE DATABASE IF NOT EXISTS testsystem_database
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE testsystem_database;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,                -- 主键

    username      VARCHAR(50)  NOT NULL UNIQUE,                     -- 登录账号
    password      VARCHAR(255) NOT NULL,                            -- 登录密码（当前为明文）

    role          ENUM('user', 'admin') NOT NULL DEFAULT 'user',    -- 角色：user / admin

    real_name     VARCHAR(50)  NOT NULL,                            -- 真实姓名
    id_card       VARCHAR(18)  NOT NULL UNIQUE,                     -- 身份证号
    gender        ENUM('男', '女') NOT NULL,                        -- 性别：男 / 女

    date_of_birth DATE           NOT NULL,                          -- 出生日期
    university    VARCHAR(100)   NOT NULL,                          -- 在读高校
    grade         VARCHAR(20)    NOT NULL,                          -- 年级（如 2022级 / 大三）

    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,      -- 创建时间
    updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP            -- 更新时间
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

> 说明：
> - 注册接口会写入除 `role` 以外的所有字段，其中 `role` 默认填 `user`。
> - 身份证号与出生日期的对应关系在前端校验，后端按前端传入的 `dateOfBirth` 持久化。

### 3. exam / exam_registration 表（预留）

> 如需扩展考试发布与报名功能，可使用类似以下结构（当前代码中仅为预研设计，可按实际需求调整）：

```sql
-- 考试表
DROP TABLE IF EXISTS `exam`;

CREATE TABLE `exam` (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,

    title           VARCHAR(100)  NOT NULL,                    -- 考试标题
    description     TEXT          NULL,                        -- 考试说明

    exam_start_time DATETIME      NOT NULL,                    -- 考试开始时间
    exam_end_time   DATETIME      NOT NULL,                    -- 考试结束时间
    location        VARCHAR(100)  NOT NULL,                    -- 考试地点

    signup_start_time DATETIME    NOT NULL,                    -- 报名开始时间
    signup_end_time   DATETIME    NOT NULL,                    -- 报名结束时间

    max_candidates  INT           NOT NULL,                    -- 报名人数上限

    created_by      BIGINT        NOT NULL,                    -- 创建人（管理员 user.id）
    status          ENUM('draft', 'open', 'closed', 'finished') NOT NULL DEFAULT 'draft',

    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_exam_creator
        FOREIGN KEY (created_by) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报名表
DROP TABLE IF EXISTS `exam_registration`;

CREATE TABLE `exam_registration` (
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,

    exam_id        BIGINT     NOT NULL,
    user_id        BIGINT     NOT NULL,

    status         ENUM('applied', 'cancelled') NOT NULL DEFAULT 'applied',
    registered_at  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_registration_exam
        FOREIGN KEY (exam_id) REFERENCES `exam`(id),
    CONSTRAINT fk_registration_user
        FOREIGN KEY (user_id) REFERENCES `user`(id),

    UNIQUE KEY uniq_exam_user (exam_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 三、主要接口说明

所有接口返回统一封装：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

- `code = 0`：成功
- `code != 0`：失败（message 中包含错误信息）

### 1. 用户登录

- 方法：`POST`
- URL：`/api/auth/login`
- 请求体：

```json
{
  "username": "admin",
  "password": "123456"
}
```

- 成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "role": "admin",
    "realName": "系统管理员",
    "gender": "男",
    "university": "系统内部",
    "grade": "N/A"
  }
}
```

- 失败响应（用户名不存在或密码错误示例）：

```json
{
  "code": 4001,
  "message": "用户名或密码错误",
  "data": null
}
```

> 当前版本未实现真正的 token/JWT，仅返回用户基本信息，由前端保存到 Pinia 中模拟登录状态。

---

### 2. 用户注册

- 方法：`POST`
- URL：`/api/auth/register`
- 请求体：

```json
{
  "username": "stu001",
  "password": "123456",
  "realName": "张三",
  "idCard": "320101200401010001",
  "gender": "女",
  "dateOfBirth": "2004-01-01",
  "university": "清华大学",
  "grade": "2022级"
}
```

> 前端会对身份证号做格式校验（18 位，最后一位为数字或大写 X），并从身份证号中自动解析出生日期填入 `dateOfBirth`，后端无需再次解析。

- 成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

- 若用户名已存在：

```json
{
  "code": 4002,
  "message": "用户名已存在",
  "data": null
}
```

---

### 3. 管理员用户管理（分页查询）

- 方法：`GET`
- URL：`/api/admin/users`
- 请求参数：

| 参数 | 类型 | 说明         |
|------|------|--------------|
| page | int  | 页码，从 1 开始，默认 1 |
| size | int  | 每页条数，默认 10 |

- 示例请求：

`GET /api/admin/users?page=1&size=10`

- 成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "total": 35,
    "records": [
      {
        "id": 1,
        "username": "admin",
        "password": "123456",
        "role": "admin",
        "realName": "系统管理员",
        "idCard": "110101199001010000",
        "gender": "男",
        "dateOfBirth": "1990-01-01",
        "university": "系统内部",
        "grade": "N/A",
        "createdAt": "2025-01-01T10:00:00",
        "updatedAt": "2025-01-01T10:00:00"
      }
    ]
  }
}
```

> 前端只展示 `id / username / role`，其余字段可用于后续扩展。

---

### 4. 管理员删除用户

- 方法：`DELETE`
- URL：`/api/admin/users/{id}`

示例：`DELETE /api/admin/users/3`

- 成功响应：

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

> 当前未对「不允许删除自己」等场景做保护逻辑，如有需要可在 Service 层加判断。

---

## 四、项目结构

主要包结构如下（省略非关键文件）：

```text
src/main/java/com/testsystem
├── TestSystemApplication.java      # Spring Boot 启动类
│
├── entity
│   └── User.java                   # 用户实体类
│   └── Exam.java                   # （预留）考试实体
│   └── ExamRegistration.java       # （预留）报名实体
│
├── mapper
│   └── UserMapper.java             # 用户 Mapper
│
├── service
│   ├── UserService.java            # 用户业务接口
│   └── impl
│       └── UserServiceImpl.java    # 用户业务实现
│
├── controller
│   ├── AuthController.java         # 登录 / 注册接口
│   └── AdminUserController.java    # 管理员用户管理接口
│
├── dto
│   ├── LoginRequest.java           # 登录请求 DTO
│   ├── LoginResponse.java          # 登录响应 DTO
│   └── RegisterRequest.java        # 注册请求 DTO
│
└── common
    ├── ApiResponse.java            # 统一返回结果封装
    └── PageResult.java             # 分页结果封装
```

MyBatis 映射文件：

```text
src/main/resources/mapper
└── UserMapper.xml                  # UserMapper 对应 SQL
```

---

## 五、配置说明

示例 `src/main/resources/application.properties`：

```properties
# 应用基本信息
spring.application.name=TestSystem
server.port=8081

# 数据源配置
spring.datasource.url=jdbc:mysql://localhost:3306/testsystem_database?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=你的数据库密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MyBatis 配置
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.testsystem.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

---

## 六、本地运行步骤

1. **准备环境**

   - 安装 JDK 17
   - 安装并启动 MySQL
   - 安装 Maven

2. **初始化数据库**

   - 在 MySQL 中执行「数据库设计」部分的建库建表 SQL
   - 确认 `testsystem_database.user` 中已存在至少一个管理员账号（如前面 SQL 中插入的 `admin`）

3. **配置应用**

   - 修改 `application.properties` 中的数据库用户名和密码

4. **运行后端**

   - 使用 IDE（IntelliJ IDEA 等）运行 `TestSystemApplication`
   - 或使用 Maven 命令：

     ```bash
     mvn spring-boot:run
     ```

   - 后端默认监听 `http://localhost:8081`

5. **与前端联调**

   - 前端配置 Vite 代理，将 `/api` 转发到后端：

     ```js
     // vite.config.js
     server: {
       proxy: {
         '/api': {
           target: 'http://localhost:8081',
           changeOrigin: true
         }
       }
     }
     ```

   - 通过前端页面进行注册 / 登录 / 管理员用户管理操作。

---

## 七、后续扩展建议

- 在登录与管理员接口上引入 **JWT + Spring Security**，实现真正的权限控制；
- 完成对 `exam` / `exam_registration` 两张表的 Service / Controller 实现：
  - 管理员：发布/编辑/删除考试、查看报名名单；
  - 学生：查看可报名考试列表、报名 / 取消报名、查看历史考试记录；
- 对密码使用安全哈希（如 BCrypt）存储；
- 对 `id_card` 做更严格的校验（包括校验码计算），配合前端身份证校验逻辑；
- 增加统一异常处理、日志记录与接口文档（Swagger / SpringDoc）。