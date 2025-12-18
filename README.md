# TODO Collaboration Application

一个基于Java的协作式任务管理应用。

## 安装说明

### 环境要求
- Java 17 或更高版本
- Maven 3.8.x 或更高版本
- MySQL 8.0 或更高版本
- Kafka 2.8.x 或更高版本
- Redis 5.0 或更高版本

### 构建项目
1. 克隆项目到本地：
```bash
git clone <repository-url>
```
2. 进入项目目录：
```bash
cd todo-collaboration 
```
3. 使用Maven构建项目：
```bash
mvn clean install 
```
### 数据库配置
1. 创建MySQL数据库和表：
```sql
CREATE DATABASE todo_collaboration;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `list_id` bigint DEFAULT NULL COMMENT '列表id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `permission_type` varchar(12) DEFAULT NULL COMMENT '权限类型：view/edit',
  `granted_by` bigint DEFAULT NULL COMMENT '授权者id',
  `created_at` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `todo_lists` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL COMMENT '列表标题',
  `description` varchar(45) DEFAULT NULL COMMENT '列表描述',
  `owner_id` bigint DEFAULT NULL COMMENT '所有者id',
  `visibility` varchar(10) DEFAULT NULL COMMENT '可见性：public/private',
  `priority` int DEFAULT NULL COMMENT '优先级1-5',
  `due_date` date DEFAULT NULL COMMENT '截止日期',
  `status` varchar(20) DEFAULT NULL COMMENT '状态：todo/in_progress/done',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `last_activity` datetime DEFAULT NULL COMMENT '最后活动时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```

2. 在 `application.properties` 文件中配置数据库连接信息：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_collaboration
spring.datasource.username=your_username 
spring.datasource.password=your_password
```

### 安装kafka

### 安装redis

## 使用说明

### 启动应用
运行以下命令启动应用程序：
```bash
mvn package 
java -jar target/todo-collaboration-*.jar
```

### API访问
应用启动后，默认访问地址为：
- Swagger UI文档: http://localhost:8081/swagger-ui/index.html

### 主要功能
- 用户注册与登录
- 创建、查看、更新和删除个人任务
- 邀请其他用户协作完成任务
- 分配任务给团队成员
- 设置任务优先级和截止日期
- 实时查看任务状态更新

### 认证与授权
- 使用JWT token进行身份验证
- 注册新用户后获取访问令牌
- 在每个API请求的header中添加Authorization: Bearer <token>

### 示例操作流程
1. 注册新用户
2. 登录获取访问令牌
3. 创建新的待办事项
4. 邀请其他用户加入任务协作
5. 更新任务状态和进度
6. 查看任务历史记录和活动日志
