# 建检智通 · 建筑行业检测仪器智能管理系统

> 基于 Spring Boot + Vue3 的 B/S 架构仪器设备全生命周期管理系统

## 项目简介

面向建筑检测机构，围绕仪器设备「采购 → 台账 → 入库 → 领用归还 → 校准 → 报修/报废 → 统计」的全生命周期管理需求，构建一套覆盖设备管理员、试验室主管、项目检测员三类角色的管理系统，替代纸质台账与 Excel 登记，提升仪器管理效率与数据可追溯性。

## 核心功能

**设备管理员（ADMIN）**
- 仪器档案管理：录入、编辑仪器设备信息
- 库存管理：仓库库存、工地出入库登记
- 采购录入：采购合同、到货验收、发票附件录入
- 校准记录：校准到期提醒、校准记录录入
- 异常处理：处理检测员提交的设备异常申请，按类型分流报修或报废
- 用户管理：新增、停用项目检测员账号

**试验室主管（MANAGER）**
- 审批管理：采购单据审核、报废申请终审、报修审批
- 库存预警：全企业库存短缺预警
- 采购汇总：采购需求自动汇总、多维度检索
- 报表统计：设备总数、分类统计、大小型设备占比、报修/报废数据可视化
- 账号管控：管理全部三类业务账号

**项目检测员（TESTER）**
- 领用申请：提交所属工地仪器领用申请
- 异常申请：提交设备异常申请
- 我的记录：查看本人领用与异常申请记录

## 技术栈

**后端**
- Java 8 / Spring Boot 2.7.18
- Spring Data JPA / Hibernate
- Spring Security + JWT 认证
- MySQL 5.7+ / Lombok / Maven

**前端**
- Vue 3 + Vite 5
- Element Plus + ECharts + Vue ECharts + Sass
- Vue Router + Axios

## 目录结构

```
building-instrument-management/
├── 开发代码/
│   ├── backend/                # Spring Boot 后端
│   │   └── src/main/java/com/example/device/
│   │       ├── config / controller / dto / entity / repository / service / util
│   │   └── src/main/resources/ # application.yml、data.sql、schema.sql
│   └── frontend/               # Vue 3 前端
│       └── src/                # api / router / views / App.vue / main.js / style.scss
├── 1.项目需求调研报告.docx
├── 2-1 系统需求规格说明书.doc
├── 2-2 系统设计说明书.doc
└── 核心代码.docx
```

## 快速开始

### 1. 准备数据库

创建数据库 `device_management`，并修改 `开发代码/backend/src/main/resources/application.yml` 中的数据库连接配置（密码请使用本地配置或环境变量注入，勿提交明文）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/device_management?useUnicode=true&characterEncoding=utf-8
    username: root
    password: your_password
```

### 2. 启动后端

```bash
cd 开发代码/backend
mvn spring-boot:run
```

服务运行在 `http://localhost:8081`。

### 3. 启动前端

```bash
cd 开发代码/frontend
npm install
npm run dev
```

浏览器访问前端输出的地址（默认 `http://localhost:3000`），使用已注册的账号登录。

> 演示账号密码已从文档中移除，请通过后端初始化脚本或自行注册获取。部署到公开环境前务必修改默认密码。

## 说明

本项目为课程实践作品，演示密码与敏感配置已从代码和文档中清理，请以占位符或本地配置方式注入后再运行。配套的需求调研、规格说明书、设计说明书与核心代码文档见仓库根目录。
