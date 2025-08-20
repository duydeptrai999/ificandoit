# Base AI Project 🚀

> **Dự án AI cơ sở với hệ thống Agent thông minh và workflow tự động**

## 🎯 Giới thiệu

Base AI Project là một framework toàn diện cho việc phát triển ứng dụng AI với hệ thống Agent Selection thông minh, workflow tự động và quản lý task hiệu quả. Dự án được thiết kế để tối ưu hóa quy trình phát triển và đảm bảo chất lượng code cao.

## 🌟 Tính năng chính

- **🤖 Agent Selector System**: Hệ thống chọn agent thông minh dựa trên context và yêu cầu
- **⚡ Workflow tự động**: Quy trình phát triển được tự động hóa với Kiro Task System
- **📱 Multi-platform**: Hỗ trợ phát triển iOS, Android, Web và Cross-platform
- **🔧 Code Quality**: Tích hợp TDD, code review và best practices
- **📊 Task Management**: Quản lý task thông minh với auto-expansion và tracking
- **🌐 i18n Support**: Hỗ trợ đa ngôn ngữ tích hợp

## 🚀 Bắt đầu nhanh

### Bước 1: Tạo Agent Selector

**Điều đầu tiên và quan trọng nhất** - Tạo Agent Selector để tối ưu hóa workflow:

🎉 **Tạo Agent Selector vui nhộn với TRAE!**

👉 **[Agent Selector Main](.trae/agents/agent-selector-system.md)**
👉 **[Agent Selector Main Yolo](.trae/agents/agent-selector-yolo-mode.md)**

Tạo agent của Trae và copy prompt vào

Agent Selector sẽ giúp bạn:

- Tự động chọn agent phù hợp cho từng task
- Phân tích context và đưa ra gợi ý tối ưu
- Tăng hiệu quả làm việc lên 300%
- Giảm thiểu lỗi và tối ưu hóa workflow

### Bước 2: Cấu trúc dự án

```
Base-AI-Project/
├── .trae/                    # 🎯 CORE - Cấu hình chính của dự án
│   │   agents/         # Định nghĩa các agents
│   ├── rules/               # Quy tắc và workflow
│   │   ├── core/           # Quy tắc cốt lõi
│   │   ├── mobile/         # Quy tắc mobile development
│   │   └── workflows/      # Các workflow tự động
│   ├── config/             # Cấu hình hệ thống
│   └── scripts/            # Scripts tiện ích
├── .kiro/                   # 📋 Task Management System
│   ├── specs/              # Specifications cho từng dự án
│   └── steering/           # Hướng dẫn và templates
├── docs/                    # 📚 Tài liệu dự án
├── code_base/              # 💻 Source code
└── tools/                  # 🛠️ Công cụ hỗ trợ
```

## 🎯 Hệ thống Agent Selection

### Các Agent có sẵn:

- **🍎 iOS Development Agent**: Phát triển ứng dụng iOS native
- **🤖 Android Development Agent**: Phát triển ứng dụng Android native
- **🌐 Frontend Development Agent**: Phát triển web frontend
- **⚙️ Backend Development Agent**: Phát triển API và server
- **📱 Mobile Cross-platform Agent**: Flutter và React Native
- **📦 APK Modification Agent**: Chỉnh sửa APK và tích hợp SDK
- **🚀 DevOps Infrastructure Agent**: Deployment và CI/CD

### Cách sử dụng Agent Selector:

1. **Mô tả task của bạn** bằng ngôn ngữ tự nhiên
2. **Agent Selector tự động phân tích** context và keywords
3. **Chọn agent phù hợp nhất** với confidence score
4. **Thực hiện task** với workflow được tối ưu hóa

## 📋 Kiro Task Management System

### Tính năng chính:

- **Auto Task Creation**: Tự động tạo task từ yêu cầu
- **Smart Expansion**: Mở rộng task thành sub-tasks chi tiết
- **Progress Tracking**: Theo dõi tiến độ real-time
- **Dependency Management**: Quản lý phụ thuộc giữa các task

### Workflow cơ bản:

```
Yêu cầu → Brainstorm → Requirements → Design → Tasks → Implementation
```

### TSDDR 2.0 Workflow:

**⚠️ Quan trọng**: Mọi dự án hiệu quả cao đều cần có TSDDR 2.0 Workflow!

Agent Selector sẽ tự động kiểm tra sự tồn tại của workflow này và yêu cầu tạo mới nếu chưa có:

- **Tự động rà soát**: Kiểm tra tài liệu workflow trong dự án
- **Brainstorm hỗ trợ**: Đề xuất tạo workflow nếu thiếu
- **Tối ưu hiệu quả**: Đảm bảo chất lượng và tốc độ phát triển
- **Integration**: Tích hợp với existing development workflow

## 🎨 Prompt mẫu

### TSDDR 2.0 Workflow

```
Tạo TSDDR 2.0 Workflow cho dự án [PROJECT_NAME].
Workflow này cần bao gồm:
- Quy trình tạo test specifications trước khi development
- Review process cho test specs và implementation
- Quality gates và acceptance criteria
- Integration với existing development workflow
- Automation và tooling requirements
- Performance metrics và success indicators

Dự án hiện tại sử dụng [TECH_STACK] và có [PROJECT_COMPLEXITY] complexity level.
```

### Mobile App Development

```
Tạo mobile app [APP_NAME] với các tính năng [FEATURES_LIST].
Target platforms: [iOS/Android/Both]
Tech stack preference: [Flutter/React Native/Native]
Complexity: [Simple/Medium/Complex]
```

### API Development

```
Tạo RESTful API cho [DOMAIN] với endpoints:
- [ENDPOINT_1]: [DESCRIPTION]
- [ENDPOINT_2]: [DESCRIPTION]
Database: [DATABASE_TYPE]
Authentication: [AUTH_METHOD]
```

### DevOps Setup

```
Setup CI/CD pipeline cho [PROJECT_TYPE] project:
- Source: [GIT_PROVIDER]
- Build: [BUILD_TOOLS]
- Deploy: [DEPLOYMENT_TARGET]
- Monitoring: [MONITORING_TOOLS]
```

## 📚 Tài liệu chi tiết

- **[Agent System Guide](.trae/rules/agents/README.md)**: Hướng dẫn chi tiết về hệ thống agent
- **[Agent Selector System](.trae/rules/agents/agent-selector-system.md)**: Hệ thống chọn agent thông minh
- **[Workflow Documentation](.trae/rules/workflows/README.md)**: Tài liệu về các workflow
- **[Kiro Task System](.kiro/README.md)**: Hướng dẫn sử dụng Kiro
- **[Development Rules](.trae/rules/core/README.md)**: Quy tắc phát triển

---

**🎉 Chúc bạn coding vui vẻ với Base AI Project!**

> _"The best way to predict the future is to create it with AI"_ 🚀
