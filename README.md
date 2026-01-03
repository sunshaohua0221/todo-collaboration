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
git clone https://github.com/sunshaohua0221/todo-collaboration.git
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
  `version` int DEFAULT 1 COMMENT '版本号，乐观锁',
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
启动
```bash
cd kafka_2.12-3.8.0
./bin/kafka-server-start.sh -daemon config/kraft/server.properties
```

### 安装redis
```bash
cd redis-7.0.15
redis-server redis.conf
```

## 使用说明

### 启动应用
运行以下命令启动应用程序：
```bash
mvn package 
java -jar target/todo-collaboration-*.jar
```

测试stomp订阅：
http://localhost:8081
连接协议：SockJs

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


演示步骤：
1、注册
2、登录
3、查询TODO任务
4、创建TODO任务
5、修改TODO任务
6、删除TODO任务
7、授权
8、多用户协作



websocket 和 websocket stomp区别：
WebSocket 作为底层传输协议‌，它是一种在单个TCP连接上进行全双工通信的应用层协议，允许数据在客户端和服务器之间双向实时流动。其连接建立过程通过 HTTP 请求升级实现，之后通信复用该 TCP 连接，适用于实时聊天、游戏同步等需要低延迟交互的场景。‌
STOMP over WebSocket 提供高层级消息语义‌，它在 WebSocket 之上添加了结构化消息框架，使用基于帧（frame）的格式定义命令如 SEND、SUBSCRIBE、MESSAGE 等，使客户端和服务器能以标准化方式发送、订阅或确认消息。这种设计降低了实时通信的复杂性，尤其适合需要发布/订阅模型或消息队列的Web应用。‌
‌两者关系可类比为 TCP 与 HTTP‌，WebSocket 类似 TCP，提供原始数据传输通道；STOMP 则类似 HTTP，在 WebSocket 的基础上定义了“请求-响应”式的交互规则。实际应用中，STOMP 通常依赖 WebSocket 作为传输载体（例如通过Spring框架的 Stomp.over(socket) 封装），以兼顾底层效率和高层抽象。‌


特性	            MongoDB (NoSQL)	            MySQL (关系型数据库)
数据模型	        文档模型（BSON/JSON）	        表格模型（行和列）
模式（Schema）	灵活的动态模式（Schema-less）	严格的预定义模式（Schema）
查询语言	        基于 JSON 的查询语法	        SQL（结构化查询语言）
扩展性	        天然支持水平扩展（分片）	    通常垂直扩展，水平扩展较复杂
事务支持	        支持多文档事务（从 4.0 版本开始）	完整支持 ACID 事务
JOIN 操作	    无原生 JOIN，需用 $lookup 聚合	原生支持 JOIN
适用场景	        非结构化/半结构化数据、高写入吞吐量	结构化数据、复杂事务、强一致性需求
