# 建检智通 · 建筑行业检测仪器智能管理系统

> 基于 Spring Boot + Vue 3 的 B/S 架构仪器设备全生命周期管理系统

| Spring Boot 2.7 | Vue 3 | Spring Data JPA | Spring Security + JWT | MySQL | ECharts |
|---|---|---|---|---|---|

## 一、项目概述

面向建筑检测机构，围绕仪器设备「**采购 → 台账 → 入库 → 领用归还 → 校准 → 报修/报废 → 统计**」的全生命周期管理需求，构建一套覆盖**设备管理员、试验室主管、项目检测员**三类角色的管理系统，替代纸质台账与 Excel 登记，提升仪器管理效率与数据可追溯性。

**项目规模**：11 个数据库实体、11 个业务模块、约 75 个 RESTful 接口、3 类角色权限隔离。

## 二、核心功能

### 设备管理员（ADMIN）

- **仪器台账**：录入、编辑、查询仪器设备档案（类型/型号/厂商/价值/所属实验室）
- **库存管理**：仓库库存、工地出入库登记，库存流水记录
- **采购录入**：采购申请、到货验收、发票附件、采购汇总
- **校准管理**：校准记录录入、校准到期提醒
- **异常处理**：处理检测员提交的设备异常申请，按设备类型分流为**报修**或**报废**
- **用户管理**：新增、停用项目检测员账号

### 试验室主管（MANAGER）

- **审批管理**：采购单据审核、报废申请终审、报修审批
- **库存预警**：全企业库存短缺预警
- **采购汇总**：采购需求自动汇总、多维度检索
- **报表统计**：设备总数、分类统计、大型/小型设备占比、报修/报废/领用数据统计（ECharts 可视化）
- **账号管控**：管理全部三类业务账号

### 项目检测员（TESTER）

- **领用申请**：提交所属工地仪器领用申请
- **异常申请**：提交设备异常申请（不区分报修/报废，由管理员后续分流）
- **我的记录**：查看本人领用与异常申请记录

## 三、技术架构

```
浏览器 (Vue 3 + Vite + Element Plus + ECharts + Sass)
        │  HTTP / JSON（Bearer JWT）
        ▼
Spring Boot 后端
  ├─ SecurityConfig        # 认证入口 + 方法级 @PreAuthorize 角色控制
  ├─ JwtUtil               # Token 签发与解析
  ├─ Controller 层         # 12 个 Controller
  ├─ Service 层            # 11 个业务服务 + 事务
  ├─ Repository 层         # Spring Data JPA
  └─ MySQL                 # 11 张实体表 + 初始化种子数据
```

**技术选型**

- **后端**：Java 8、Spring Boot 2.7.18、Spring Data JPA / Hibernate、Spring Security + JWT、MySQL、Lombok、Maven
- **前端**：Vue 3、Vite 5、Element Plus、ECharts + Vue ECharts、Sass、Vue Router、Axios
- **可视化**：ECharts 图表展示设备分类、状态占比、报修/报废统计

## 四、技术亮点

**1. 设备异常分流工作流**
检测员提交统一格式的异常申请（`DeviceIssueReport`）后，系统自动带入设备类型（LARGE / SMALL），由管理员根据类型决策生成**报修工单**（`RepairOrder`）或**报废记录**（`ScrapRecord`），形成「申请 → 分流 → 审批 → 归档」的完整链路，各状态可追踪。

**2. 设备状态机管理**
设备实体以状态字段驱动生命周期（`IN_USE / IDLE / BORROWED / REPAIRING / SCRAPPED` 等），领用、归还、报修、报废操作均更新状态，报表统计按状态聚合。

**3. 多维度报表统计**
`ReportService` 聚合设备、报修、报废、领用四类数据，一次返回设备总数、大小型占比、各状态数量、报修/报废统计，供主管端 ECharts 图表直接消费；统计接口统一用 `@PreAuthorize("hasRole('MANAGER')")` 保护。

**4. 方法级权限隔离**
12 个 Controller 按角色配置 `@PreAuthorize`，管理员、主管、检测员各自只能访问职责范围内的接口，从接口层杜绝越权。

**5. 丰富种子数据与开箱演示**
`DataInitializer` 在库为空时自动灌入岩土勘察、结构检测等多类真实仪器台账（含型号、厂商、价值、所属实验室），首次启动即可看到完整业务数据。

## 五、目录结构

```
building-instrument-management/
├── 开发代码/
│   ├── backend/
│   │   └── src/main/java/com/example/device/
│   │       ├── config/       # Security / Cors / DataInitializer
│   │       ├── controller/   # 12 个 Controller
│   │       ├── dto/          # 请求与响应对象
│   │       ├── entity/       # 11 个实体
│   │       ├── repository/   # JPA Repository
│   │       ├── service/      # 11 个业务服务
│   │       └── util/         # JwtUtil / PasswordGenerator
│   └── frontend/
│       └── src/              # api / router / views / style.scss
├── 1.项目需求调研报告.docx
├── 2-1 系统需求规格说明书.doc
├── 2-2 系统设计说明书.doc
└── 核心代码.docx
```

## 六、快速开始

**环境要求**：JDK 8+、Maven 3.6+、Node.js 16+、MySQL 5.7+

**1. 准备数据库**

创建数据库 `device_management`，并修改 `开发代码/backend/src/main/resources/application.yml` 中的连接配置（密码通过本地配置或环境变量注入）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/device_management?useUnicode=true&characterEncoding=utf-8
    username: root
    password: your_password
```

**2. 启动后端**

```bash
cd 开发代码/backend
mvn spring-boot:run
```

服务运行在 `http://localhost:8081`。

**3. 启动前端**

```bash
cd 开发代码/frontend
npm install
npm run dev
```

**局域网多人使用**：用 `ipconfig` 查服务器 IP，修改 `frontend/vite.config.js` 中代理 `target` 为该 IP，其他用户访问 `http://服务器IP:端口`。

> 演示账号密码已从代码与文档中移除，请通过初始化脚本或自行注册获取；部署公开环境前务必修改默认密码。

## 七、说明

本项目为课程实践作品，配套需求调研报告、规格说明书、设计说明书与核心代码文档见仓库根目录。所有演示密码与敏感配置已清理，请以占位符或本地配置方式注入后再运行。
