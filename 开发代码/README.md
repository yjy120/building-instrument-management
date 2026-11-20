# 建筑检测机构仪器设备管理系统

## 项目简介

本系统是一个基于B/S架构的建筑检测机构仪器设备管理系统，实现仪器设备的全生命周期管理，包括仪器台账、库存管理、领用归还、故障报修、校准记录、报废申请、采购管理等功能。

## 技术架构

### 后端
- Java 8
- Spring Boot 2.7.18
- Spring Security + JWT 认证
- JPA/Hibernate
- MySQL 数据库

### 前端
- Vue 3
- Element Plus UI组件库
- Vite 构建工具
- Axios HTTP客户端
- Vue Router 路由管理

## 功能模块

### 设备管理员 (ADMIN)
- 仪器档案管理：录入、编辑仪器设备信息
- 库存管理：仓库库存、工地出入库登记
- 采购录入：采购合同、到货验收、发票附件录入
- 校准记录：仪器校准到期提醒、校准记录录入
- 异常申请处理：处理检测员提交的设备异常申请，根据设备类型选择报修或报废
- 用户管理：新增、停用项目检测员账号

### 试验室主管 (MANAGER)
- 审批管理：采购单据审核、报废申请终审、报修审批
- 库存预警：全企业库存短缺预警
- 采购汇总：采购需求自动汇总、多维度检索
- 报表统计：设备总数、分类统计、大型/小型设备占比、报修/报废统计数据可视化
- 账号管控：管理全部三类业务账号

### 项目检测员 (TESTER)
- 领用申请：提交所属工地仪器领用申请
- 异常申请：提交设备异常申请（不区分报修/报废，由管理员处理）
- 我的记录：查看本人领用、异常申请记录

## 系统账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | YOUR_ADMIN_PASSWORD | 设备管理员 |
| manager | YOUR_MANAGER_PASSWORD | 试验室主管 |
| tester1 | YOUR_TESTER_PASSWORD | 项目检测员 |
| tester2 | YOUR_TESTER_PASSWORD | 项目检测员 |

## 运行说明

### 环境要求
- JDK 8+
- Maven 3.6+
- Node.js 16+
- MySQL 5.7+

### 数据库配置
1. 创建数据库：`device_management`
2. 修改 `backend/src/main/resources/application.yml` 中的数据库连接配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/device_management?useUnicode=true&characterEncoding=utf-8
    username: root
    password: your_password
```

### 启动后端
```bash
cd backend
mvn spring-boot:run
```
后端服务运行在 `http://localhost:8081`

### 启动前端
```bash
cd frontend
npm install
npm run dev
```
前端服务运行在 `http://localhost:3000`（或其他可用端口）

### 访问系统
浏览器访问 `http://localhost:3000`，使用上述账号登录

### 局域网访问（多人使用）
1. 获取服务器电脑的局域网IP地址：
   ```bash
   ipconfig | findstr "IPv4"
   ```
2. 修改 `frontend/vite.config.js` 中的代理目标地址为服务器IP：
   ```javascript
   target: 'http://服务器IP:8081'
   ```
3. 其他用户访问：`http://服务器IP:3000`

## 项目结构

```
├── backend/                    # 后端项目
│   ├── src/main/java/          # Java源代码
│   │   └── com/example/device/
│   │       ├── config/         # 配置类（安全、CORS等）
│   │       ├── controller/     # 控制器（API接口）
│   │       ├── dto/            # 数据传输对象
│   │       ├── entity/         # 实体类
│   │       ├── repository/     # 数据访问层
│   │       ├── service/        # 业务逻辑层
│   │       └── util/           # 工具类
│   ├── src/main/resources/     # 配置文件
│   │   ├── application.yml     # 应用配置
│   │   ├── data.sql            # 初始数据
│   │   └── schema.sql          # 数据库结构
│   └ pom.xml                   # Maven依赖配置
│
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API接口定义
│   │   ├── router/             # 路由配置
│   │   ├── views/              # 页面组件
│   │   ├── App.vue             # 主应用组件
│   │   ├── main.js             # 入口文件
│   │   └── style.scss          # 全局样式
│   ├── index.html              # HTML模板
│   ├── package.json            # npm依赖配置
│   └── vite.config.js          # Vite配置
│
└ README.md                     # 项目说明文档
```

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录

### 设备接口
- `GET /api/device` - 获取设备列表
- `GET /api/device/active` - 获取在用设备列表
- `POST /api/device` - 新增设备（ADMIN）
- `PUT /api/device/{id}` - 编辑设备（ADMIN）

### 库存接口
- `GET /api/stock` - 获取库存列表
- `POST /api/stock/adjust` - 调整库存（ADMIN）

### 领用接口
- `GET /api/use-record` - 获取领用记录列表
- `GET /api/use-record/user/{userId}` - 获取用户领用记录
- `POST /api/use-record` - 提交领用申请（ADMIN, TESTER）
- `PUT /api/use-record/return/{id}` - 归还设备

### 异常申请接口
- `GET /api/issue-report` - 获取异常申请列表（ADMIN）
- `GET /api/issue-report/pending` - 获取待处理异常申请（ADMIN）
- `GET /api/issue-report/user/{userId}` - 获取用户异常申请记录（TESTER）
- `POST /api/issue-report` - 提交异常申请（TESTER）
- `POST /api/issue-report/{id}/handle-repair` - 转为报修（ADMIN）
- `POST /api/issue-report/{id}/handle-scrap` - 转为报废（ADMIN）

### 报修接口
- `GET /api/repair` - 获取报修工单列表
- `GET /api/repair/pending` - 获取待审批报修工单（MANAGER）
- `GET /api/repair/user/{userId}` - 获取用户报修记录
- `POST /api/repair` - 提交报修申请（ADMIN）
- `PUT /api/repair/{id}` - 更新报修状态（MANAGER）

### 报废接口
- `GET /api/scrap` - 获取报废记录列表
- `GET /api/scrap/pending` - 获取待审批报废记录（MANAGER）
- `POST /api/scrap` - 提交报废申请（ADMIN）
- `PUT /api/scrap/{id}` - 审批报废（MANAGER）

### 校准接口
- `GET /api/calibration` - 获取校准记录列表
- `POST /api/calibration` - 录入校准记录（ADMIN）

### 报表接口（MANAGER）
- `GET /api/report/all` - 获取所有统计数据
- `GET /api/report/device` - 设备统计
- `GET /api/report/repair` - 报修统计
- `GET /api/report/scrap` - 报废统计
- `GET /api/report/use` - 使用记录统计

### 采购接口
- `GET /api/purchase` - 获取采购记录列表
- `POST /api/purchase` - 录入采购记录（ADMIN）

### 用户接口
- `GET /api/user` - 获取用户列表（ADMIN, MANAGER）
- `POST /api/user` - 新增用户
- `PUT /api/user/{id}` - 编辑用户
- `DELETE /api/user/{id}` - 删除用户

## 权限控制

系统采用基于角色的访问控制（RBAC），通过Spring Security的`@PreAuthorize`注解实现接口级别的权限控制。

- ADMIN：仅录入权限，无审批权限
- MANAGER：仅审批权限，无录入权限
- TESTER：仅提交申请权限，无录入、审批权限

## 开发说明

### 新增功能模块
1. 在 `backend/src/main/java/com/example/device/entity/` 创建实体类
2. 在 `backend/src/main/java/com/example/device/repository/` 创建Repository
3. 在 `backend/src/main/java/com/example/device/service/` 创建Service
4. 在 `backend/src/main/java/com/example/device/controller/` 创建Controller
5. 在 `frontend/src/views/` 创建Vue页面组件
6. 在 `frontend/src/router/index.js` 添加路由
7. 在 `frontend/src/api/index.js` 添加API接口定义

### 权限配置
在Controller方法上添加`@PreAuthorize`注解：
```java
@PreAuthorize("hasRole('ADMIN')")           // 仅ADMIN可访问
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")  // ADMIN和MANAGER可访问
```