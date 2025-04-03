# SOLO领域定制

## 项目概述
本项目是一个面向场景计算的低代码平台，支持领域建模、场景定制和设备管理等功能。系统分为三个层次：元工具平台、领域平台和场景平台，形成一个完整的低代码开发生态系统。

## 功能特性
- 元工具平台：创建和管理领域模型
- 领域平台：基于领域模型创建场景
- 场景平台：管理设备和数据流、可视化监控

## 开发环境
- Node.js >= 16.0.0
- Vue 3.5+
- TypeScript
- Vite
- Element Plus

## 项目结构

## 后端接口文档

### 用户认证
- `/auth/login` (POST) - 用户登录
  - 请求体: `{ username: string, password: string }`
  - 响应: `{ code: number, message: string, data: { token: string, user: User } }`
- `/auth/logout` (POST) - 用户登出
  - 响应: `{ code: number, message: string }`
- `/auth/user` (GET) - 获取当前用户信息
  - 响应: `{ code: number, message: string, data: User }`

### 领域管理
- `/domains` (GET) - 获取所有领域列表
  - 响应: `{ code: number, message: string, data: Domain[] }`
- `/domains/{id}` (GET) - 获取指定ID的领域
  - 响应: `{ code: number, message: string, data: Domain }`
- `/domains` (POST) - 创建新领域
  - 请求体: `{ name: string, description: string, status: string }`
  - 响应: `{ code: number, message: string, data: Domain }`
- `/domains/{id}` (PUT) - 更新领域信息
  - 请求体: `{ name?: string, description?: string, status?: string }`
  - 响应: `{ code: number, message: string, data: Domain }`
- `/domains/{id}` (DELETE) - 删除领域
  - 响应: `{ code: number, message: string }`

### 场景管理
- `/scenes` (GET) - 获取场景列表
  - 参数: `domainId?: number` - 可选，按领域ID筛选
  - 响应: `{ code: number, message: string, data: Scene[] }`
    - `Scene` 包括以下字段:
      - `id: number`
      - `name: string`
      - `description: string`
      - `status: string`
      - `domainId: number`
      - `location: { lng: number, lat: number }` - 场景的坐标位置
- `/scenes/{id}` (GET) - 获取指定ID的场景
  - 响应: `{ code: number, message: string, data: Scene }`
    - `Scene` 包括以下字段:
      - `id: number`
      - `name: string`
      - `description: string`
      - `status: string`
      - `domainId: number`
      - `location: { lng: number, lat: number }` - 场景的坐标位置
- `/scenes` (POST) - 创建新场景
  - 请求体: `{ name: string, description: string, status: string, domainId: number, location: { lng: number, lat: number } }`
  - 响应: `{ code: number, message: string, data: Scene }`
- `/scenes/{id}` (PUT) - 更新场景信息
  - 请求体: `{ name?: string, description?: string, status?: string, location?: { lng: number, lat: number } }`
  - 响应: `{ code: number, message: string, data: Scene }`
- `/scenes/{id}` (DELETE) - 删除场景
  - 响应: `{ code: number, message: string }`

### 设备管理
- `/devices` (GET) - 获取设备列表
  - 参数: `sceneId?: number` - 可选，按场景ID筛选
  - 响应: `{ code: number, message: string, data: Device[] }`
- `/devices/{id}` (GET) - 获取指定ID的设备
  - 响应: `{ code: number, message: string, data: Device }`
- `/devices` (POST) - 创建新设备
  - 请求体: `{ name: string, type: string, status: string, sceneId: number, location: { x: number, y: number }, properties?: Record<string, any> }`
  - 响应: `{ code: number, message: string, data: Device }`
- `/devices/{id}` (PUT) - 更新设备信息
  - 请求体: `{ name?: string, type?: string, status?: string, location?: { x: number, y: number }, properties?: Record<string, any> }`
  - 响应: `{ code: number, message: string, data: Device }`
- `/devices/{id}` (DELETE) - 删除设备
  - 响应: `{ code: number, message: string }`

### 响应码说明
- 200: 成功
- 400: 请求参数错误
- 401: 未认证或认证失效
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

### question
1、需要每个菜单页面单独一个项目打包吗，还是可以都放在一个项目里
打包配置怎么配能和商业版一样让打包制品的每个页面有对应的打包制品
2、如何获取全局变量（如用户信息
3、部署到商业版后页面间如何跳转，页面间如何交互
