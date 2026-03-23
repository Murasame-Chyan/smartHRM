# SmartHRM

SmartHRM 是一个基于 Spring Boot 和 Spring Cloud 的微服务架构人力资源管理系统，旨在帮助企业高效管理其员工、技能、项目及培训等信息。

## 多语言目录 Multi-Language Index
- [README.md](../README.md) - English
- [README.ja.md](README.ja.md) - Japanese
- README.zh-CN.md - Here

## 功能概览

- **员工管理**：支持员工的增删改查操作。
- **技能匹配**：根据项目需求技能，智能匹配合适的员工。
- **项目检索**：根据项目名字段或参与员工id模糊搜索项目。
- **任务分配**：对项目成员分配任务，统计完成率。
- **部门与项目管理**：管理组织架构和项目信息。
- **培训管理**：管理员工培训记录。

## 技术栈

### 后端技术栈
- **Spring Boot 3.5.7** - 应用框架
- **Spring Cloud 2025.0.1** - 微服务框架
- **Spring Cloud Alibaba 2025.0.0.0** - 阿里云集成
- **Spring Cloud Gateway** - API 网关
- **Nacos** - 服务注册与配置中心
- **MongoDB** - 数据库
- **Lombok** - 代码简化

### 前端技术栈
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Element Plus** - Vue 3 组件库
- **Vue Router** - 官方路由管理器
- **Axios** - HTTP 客户端

## 微服务架构

SmartHRM 采用微服务架构，包含以下服务：

### 服务列表

| 服务名称 | 端口 | 描述 |
|---------|------|------|
| GatewayService | 8090 | API 网关，用于路由请求 |
| DepartmentService | 8081 | 部门管理服务 |
| EmployeeService | 8082 | 员工管理服务 |
| ProjectService | 8083 | 项目和任务管理服务 |
| SkillService | 8084 | 技能和技能匹配服务 |
| TrainingService | 8085 | 培训管理服务 |
| Nacos | 8848 | 服务注册与配置中心 |

### 架构概述

系统使用 Spring Cloud Gateway 作为统一入口，根据 URL 路径将请求路由到不同的微服务。所有服务都在 Nacos 中注册以实现服务发现。前端 Vue 应用通过网关与后端服务通信。

## 模块说明

### 核心服务

- **DepartmentService**：管理部门信息，包括部门的增删改查操作和员工调动。
- **EmployeeService**：管理员工信息，包括基本的增删改查操作。
- **ProjectService**：管理项目和任务信息，基本增删改查进行项目增设、任务分配、项目进度统计。
- **SkillService**：管理技能信息，并提供技能匹配功能。
- **SkillMatchController**：技能匹配功能，支持根据项目所需技能搜索合适员工。
- **TrainingService**：管理培训信息，包括培训新增。
- **GatewayService**：API 网关，将请求路由到相应的微服务。

### 公共模块

- **实体类**：核心数据模型，包括 Employee、Skill、Project、Department、Task 和 Training。
- **Dao**：直接访问 MongoDB 操作和各类查询（模糊、分页查询）。
- **Repo**：实体类数据访问层。
- **DTO**：简易数据传输对象，放置小型数据传输对象减少冗余数据传输量，安置数据传输方法和特殊处理方法。

### 实体类

- **Employee**：员工实体，包含姓名、所属部门、技能列表等信息。
- **Skill**：技能实体，包含技能名称、类型等信息。
- **Project**：项目实体，包含项目名称、成员列表、所需技能等信息。
- **Department**：部门实体，包含部门名称、经理ID、员工列表等信息。
- **Task**：项目内多名员工分配任务。
- **Training**：指定多名员工培训课程。

## 快速开始

### 环境要求

- Java 17
- MongoDB
- Maven
- Node.js（用于前端）
- Nacos Server

### 安装与配置

#### 1. 安装并启动 Nacos

从 [Nacos GitHub](https://github.com/alibaba/nacos/releases) 下载 Nacos 并启动：

```bash
# Windows
startup.cmd

# Linux/Mac
sh startup.sh -m standalone
```

访问 Nacos 控制台：http://localhost:8848/nacos
注意：生产环境请修改默认凭据以确保安全。

#### 2. 配置数据库

启动 MongoDB 服务：

```bash
# Windows
net start MongoDB

# Linux/Mac
mongod --dbpath /path/to/data
```

在每个服务的 `application.yml` 中配置 MongoDB 连接：
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/smartHRM
```

#### 3. 配置 Nacos 地址

每个微服务的 `application.yml` 应包含：
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

### 启动项目

#### 启动后端服务

**方式 1：使用 IDE**
1. 在 IntelliJ IDEA 或 Eclipse 中打开项目
2. 按顺序运行每个服务的主应用类：
   - DepartmentApplication.java
   - EmployeeApplication.java
   - ProjectApplication.java
   - SkillApplication.java
   - TrainingApplication.java
   - gatewayApplication.java（网关）

**方式 2：使用 Maven**
```bash
# 构建所有模块
mvn clean package

# 运行每个服务（在相应模块目录下）
java -jar target/DepartmentService.jar
java -jar target/EmployeeService.jar
java -jar target/ProjectService.jar
java -jar target/SkillService.jar
java -jar target/TrainingService.jar
java -jar target/GatewayService.jar
```

#### 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 访问系统

- **前端应用**：http://localhost:3000
- **API 网关**：http://localhost:8090
- **Nacos 控制台**：http://localhost:8848/nacos

## 使用说明

### 前端路由

- **员工管理**：访问 `/employees` 路由，可进行员工的添加、修改、删除操作。
- **部门管理**：访问 `/departments` 路由，可进行部门的添加、修改、删除操作。
- **技能管理**：访问 `/skills` 路由，管理技能信息。
- **培训管理**：访问 `/trainings` 路由，管理培训信息。
- **技能匹配**：访问 `/skillmatch` 路由，选择所需技能后，系统将自动匹配符合条件的员工。
- **项目匹配管理**：访问 `/projectmatch` 路由，以项目名或参与员工id模糊搜索项目详情。

### 后端 API 路由（通过网关）

所有后端 API 都通过网关 http://localhost:8090 访问：

- `/employees/**` - 员工服务
- `/departments/**` - 部门服务
- `/projects/**` - 项目服务
- `/skill/**` - 技能服务
- `/training/**` - 培训服务
- `/skillmatch/**` - 技能匹配服务
- `/projectmatch/**` - 项目匹配服务

## 配置

### 服务配置

每个微服务都有自己的 `application.yml` 文件，位于 `src/main/resources/` 目录：

- `GatewayService/src/main/resources/application.yml` - 网关路由配置
- `EmployeeService/src/main/resources/application.yml` - 员工服务配置
- `DepartmentService/src/main/resources/application.yml` - 部门服务配置
- `ProjectService/src/main/resources/application.yml` - 项目服务配置
- `SkillService/src/main/resources/application.yml` - 技能服务配置
- `TrainingService/src/main/resources/application.yml` - 培训服务配置

### 前端配置

前端 API 代理配置在 `frontend/vite.config.js` 中：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8090',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

## 贡献指南

欢迎贡献代码或提出建议！请遵循以下步骤：

1. Fork 本仓库。
2. 创建新分支 (`git checkout -b feature/new-feature`)。
3. 从远程仓库获取最新更新后将新增文件加入git版本控制。
4. 提交更改与更改描述 (`git commit -m 'Add new feature'`)。
5. 推送至远程分支 (`git push origin feature/new-feature`)。
6. 提交 Pull Request。

## 许可证

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## 演示页面

### **员工管理**： `/employees/`

#### 	总界面

[员工管理]()

#### 	新增员工

[新增员工]()

### **部门管理**： `/departments/`

#### 	总界面

[部门管理]()

#### 	增设部门

[增设部门]()

### **技能匹配**： `/skillmatch/`

[技能匹配]()

### **项目管理**： `/projectmatch/`

#### 	总界面

[项目管理]()

#### 	分配任务

[分配任务]()

### 	**技能与训练管理**： `/training/`

[技能与训练管理]()

## 其他资源

- [QUICK_START.md](../QUICK_START.md) - 详细快速启动指南
- [frontend/README.md](../frontend/README.md) - 前端文档
- [../README.md](../README.md) - 英文文档
