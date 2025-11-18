# 考试报名与管理系统（后端）

本项目是一个基于 **Spring Boot + MyBatis + MySQL** 的简易考试报名与管理系统后端，当前已实现 **用户登录** 功能，并为后续的考试发布、报名管理等功能预留了基础结构。

前端使用 Vue3 + Element Plus（运行在独立项目中），与本后端通过 JSON 接口进行交互。

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

## 二、功能概览（当前进度）

- 用户登录（/api/auth/login）
  - 支持 **普通用户（user）** 和 **管理员（admin）** 两种角色
  - 登录成功后返回用户基本信息（不包含密码、身份证等敏感信息）
- 用户信息持久化
  - 将用户数据存储到 MySQL 数据库的 `user` 表中
  - 使用 MyBatis 映射实体类 `User` 与数据库表

> 后续计划：
> - 考试信息管理（发布考试、编辑考试）
> - 报名管理（考生报名、管理员查看/审核报名）
> - 个人中心（考生查看报名记录、考试安排）

---

## 三、项目结构说明

主要包结构示意（只列出关键部分）：

```text
src/main/java/com/testsystem
├── TestSystemApplication.java      # Spring Boot 启动类
│
├── entity
│   └── User.java                   # 用户实体类
│
├── mapper
│   └── UserMapper.java             # MyBatis Mapper 接口
│
├── service
│   ├── UserService.java            # 用户相关业务接口
│   └── impl
│       └── UserServiceImpl.java    # 用户业务实现类
│
├── controller
│   └── AuthController.java         # 登录接口控制器 (/api/auth/login)
│
├── dto
│   ├── LoginRequest.java           # 登录请求 DTO
│   └── LoginResponse.java          # 登录响应 DTO
│
└── common
    └── ApiResponse.java            # 统一返回结果封装
```

MyBatis 映射文件：

```text
src/main/resources/mapper
└── UserMapper.xml                  # UserMapper 对应的 SQL 语句
```

---

## 四、数据库设计

### 1. 使用的数据库

- 数据库名：`testsystem_database`
- 主要表：
  - `user`：用户信息表（同时存储账号信息和真实信息）

### 2. user 表字段说明

| 字段名        | 类型                    | 说明                         |
|---------------|-------------------------|------------------------------|
| id            | BIGINT AUTO_INCREMENT   | 主键                         |
| username      | VARCHAR(50) UNIQUE      | 登录用户名                   |
| password      | VARCHAR(255)            | 登录密码（当前为明文存储）   |
| role          | ENUM('user','admin')    | 用户角色（普通用户/管理员）  |
| real_name     | VARCHAR(50)             | 用户真实姓名                 |
| id_card       | VARCHAR(18) UNIQUE      | 身份证号                     |
| gender        | ENUM('男','女')         | 性别                         |
| date_of_birth | DATE                    | 出生日期                     |
| university    | VARCHAR(100)            | 在读高校                     |
| grade         | VARCHAR(20)             | 年级，例如“2022级”/“大三”    |
| created_at    | DATETIME                | 记录创建时间                 |
| updated_at    | DATETIME                | 记录更新时间                 |

> 说明：  
> - 账号和真实信息目前设计在同一张表中，通过 `role` 字段区分普通用户和管理员。  
> - 后续如需更复杂的账号体系，可以拆分为账号表和用户详情表。

---

## 五、数据库初始化 SQL

可以在 MySQL 中执行以下 SQL 来初始化数据库及示例数据：

```sql
-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS testsystem_database
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE testsystem_database;

-- 3. 删除旧表（仅开发环境使用）
DROP TABLE IF EXISTS `user`;

-- 4. 创建 user 表
CREATE TABLE `user` (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,                -- 主键

    username      VARCHAR(50)  NOT NULL UNIQUE,                     -- 登录账号
    password      VARCHAR(255) NOT NULL,                            -- 登录密码

    role          ENUM('user', 'admin') NOT NULL DEFAULT 'user',    -- 角色：user / admin

    real_name     VARCHAR(50)  NOT NULL,                            -- 真实姓名
    id_card       VARCHAR(18)  NOT NULL UNIQUE,                     -- 身份证号
    gender        ENUM('男', '女') NOT NULL,                        -- 性别：男 / 女

    date_of_birth DATE           NOT NULL,                          -- 出生日期
    university    VARCHAR(100)   NOT NULL,                          -- 在读高校
    grade         VARCHAR(20)    NOT NULL,                          -- 年级

    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,      -- 创建时间
    updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP            -- 更新时间
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 插入管理员账号
INSERT INTO `user`
(username, password, role, real_name, id_card, gender, date_of_birth, university, grade)
VALUES
('admin', '123456', 'admin', '系统管理员', '110101199001010000', '男', '1990-01-01', '系统内部', 'N/A');

-- 6. 插入普通用户（考生）
INSERT INTO `user`
(username, password, role, real_name, id_card, gender, date_of_birth, university, grade)
VALUES
('stu001', '123456', 'user', '张三', '320101200401010001', '女', '2004-01-01', '清华大学', '2022级'),
('stu002', '123456', 'user', '李四', '320101200309020002', '男', '2003-09-02', '北京大学', '2021级'),
('stu003', '123456', 'user', '王五', '男', '320101200512150003', '男', '2005-12-15', '上海交通大学', '2023级');
```

---

## 六、配置说明

### 1. application.properties 示例

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

> 注意：请根据本机 MySQL 实际账号密码修改 `spring.datasource.username` 与 `spring.datasource.password`。

---

## 七、API 说明（目前已实现）

### 1. 用户登录

- 请求方法：`POST`
- URL：`/api/auth/login`
- 请求体（JSON）：

```json
{
  "username": "admin",
  "password": "123456"
}
```

- 返回体（JSON，统一封装）：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "role": "admin",
    "realName": "系统管理员"
  }
}
```

> 错误时（用户名不存在或密码错误）返回类似：

```json
{
  "code": 4001,
  "message": "用户名或密码错误",
  "data": null
}
```

---

## 八、运行步骤

1. **准备环境**
   - 安装 JDK 17
   - 安装 MySQL（并启动服务）
   - 安装 Maven（IDEA 也可以自动使用自带 Maven）

2. **初始化数据库**
   - 在 MySQL 中执行「数据库初始化 SQL」一节中的脚本
   - 确认能执行 `SELECT * FROM testsystem_database.user;` 并看到示例数据

3. **配置应用**
   - 修改 `src/main/resources/application.properties`  
     确保数据库连接信息正确（URL/用户名/密码）

4. **运行项目**
   - 使用 IDE（如 IntelliJ IDEA）运行 `TestSystemApplication.java`
   - 或在命令行运行：

     ```bash
     mvn spring-boot:run
     ```

5. **联调前端**
   - 前端项目通过 `/api` 调用本后端，例如：
     - `POST http://localhost:5173/api/auth/login`  
       （通过 Vite 代理转发到 `http://localhost:8081/api/auth/login`）

---

## 九、后续扩展建议

- 增加考试表（exam）、报名表（registration），并通过 `user_id` 关联到用户
- 基于角色（`user` / `admin`）做更细粒度的权限控制（如只能管理员发布考试）
- 登录后返回真正的登录令牌（如 JWT），目前的 token 可以先简化处理
- 对密码做加密存储（如 BCrypt），避免明文保存在数据库中

---

如需在报告或课程作业中展示项目结构，可以直接基于本 README 稍作删减或翻译。
