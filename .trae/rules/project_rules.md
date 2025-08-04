# Trae AI Project Rules

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng các rules từ `.cursor/rules/` làm nguồn chính thức cho tất cả workflows.  
> File này chỉ là alias/link đến các rules chính thức trong `.cursor/rules/`

## 🎯 Primary Rules Sources (MANDATORY)

### Core Development Rules

- **[Base Rules](../../.cursor/rules/base-rules.mdc)** - Quy tắc cơ bản cho tất cả projects
- **[Development Rules](../../.cursor/rules/development-rules.mdc)** - Quy tắc phát triển chung
- **[Development Control Rules](../../.cursor/rules/development-control-rules.mdc)** - Kiểm soát quy trình phát triển
- **[File Protection Rules](../../.cursor/rules/file-protection-rules.mdc)** - Bảo vệ và backup files

### Mobile Development Workflows

- **[Mobile Utility Workflow](../../.cursor/rules/mobile-utility-workflow.mdc)** - Workflow chính cho mobile apps
- **[Android Workflow](../../.cursor/rules/android-workflow.mdc)** - Quy trình phát triển Android
- **[iOS Workflow](../../.cursor/rules/ios-workflow.mdc)** - Quy trình phát triển iOS
- **[TDD Mobile Workflow](../../.cursor/rules/tdd-mobile-workflow.mdc)** - Test-driven development cho mobile

### Project Management

- **[Planning Workflow](../../.cursor/rules/planning-workflow.mdc)** - Quy trình lập kế hoạch
- **[Planning Enforcement](../../.cursor/rules/planning-enforcement.mdc)** - Thực thi kế hoạch
- **[Planning Validation Rules](../../.cursor/rules/planning-validation-rules.mdc)** - Xác thực kế hoạch
- **[Brainstorm Overview](../../.cursor/rules/brainstorm-overview.mdc)** - Tài liệu tổng quan về hệ thống brainstorm
- **[Enhanced Brainstorm Workflow](../../.cursor/rules/brainstorm-detailed-workflow.mdc)** - Quy trình brainstorm cơ bản được cải tiến
- **[Enhanced Expert Brainstorm Workflow](../../.cursor/rules/expert-brainstorm-workflow.mdc)** - Quy trình brainstorm với các chuyên gia được cải tiến
- **[Enhanced Brainstorm Expert Integration](../../.cursor/rules/brainstorm-expert-integration.mdc)** - Tích hợp quy trình brainstorm với nhiều chuyên gia được cải tiến
- **[Expert Brainstorm Guide](../../.cursor/rules/expert-brainstorm-guide.mdc)** - Hướng dẫn thực hiện brainstorm với nhiều chuyên gia

### Code Quality & Architecture

- **[Android Code Deduplication](../../.cursor/rules/android-code-deduplication.mdc)** - Tránh trùng lặp code Android
- **[Universal Code Deduplication](../../.cursor/rules/universal-code-deduplication.mdc)** - Tránh trùng lặp code chung
- **[Android Project Template](../../.cursor/rules/android-project-template.mdc)** - Template dự án Android
- **[iOS Project Template](../../.cursor/rules/ios-project-template.mdc)** - Template dự án iOS

### Integration & API

- **[API Integration Rules](../../.cursor/rules/api-integration-rules.mdc)** - Quy tắc tích hợp API
- **[Backend Rules](../../.cursor/rules/backend-rules-optimized.mdc)** - Quy tắc backend
- **[Frontend Rules](../../.cursor/rules/frontend-rules.mdc)** - Quy tắc frontend

### Specialized Workflows

- **[Git Workflow](../../.cursor/rules/git-workflow.mdc)** - Quy trình Git
- **[i18n Rules](../../.cursor/rules/i18n-rules.mdc)** - Quốc tế hóa
- **[Resource Management](../../.cursor/rules/resource-management.mdc)** - Quản lý tài nguyên
- **[Terminal Rules](../../.cursor/rules/terminal-rules.mdc)** - Quy tắc terminal
- **[Design to Prompt Analysis](../../.cursor/rules/design-to-prompt.mdc)** - Phân tích thiết kế và tạo prompt cho ứng dụng di động

### Project Setup & Identity

- **[Project Creation Workflow](../../.cursor/rules/project-creation-workflow.mdc)** - Tạo dự án mới
- **[Project Identity Template](../../.cursor/rules/project-identity-template.mdc)** - Template định danh dự án
- **[Project Identification Rules](../../.cursor/rules/project-identification-rules.mdc)** - Nhận diện dự án
- **[Tech Stack Selection](../../.cursor/rules/tech-stack-selection.mdc)** - Lựa chọn công nghệ

### Advanced Features

- **[Memory Bank Workflow](../../.cursor/rules/memory-bank-workflow.mdc)** - Quản lý bộ nhớ
- **[Experience System Workflow](../../.cursor/rules/experience-system-workflow.mdc)** - Hệ thống kinh nghiệm
- **[Context7 Auto Workflow](../../.cursor/rules/context7-auto-workflow.mdc)** - Tự động kiểm tra context dự án
- **[Cross-IDE Compatibility](../../.cursor/rules/cross-ide-compatibility.mdc)** - Đảm bảo hoạt động trên tất cả IDE
- **[Four Role Development](../../.cursor/rules/four-role-development.mdc)** - Phát triển 4 vai trò

### Kiro Task Execution System

- **[Kiro Task Execution](../../.cursor/rules/kiro-task-execution.mdc)** - Hệ thống thực thi task tự động
- **[Kiro Fallback Workflow](../../.cursor/rules/kiro-fallback-workflow.mdc)** - Quy trình dự phòng khi thiếu file Kiro
- **[Kiro System Overview](../../.cursor/rules/kiro-system-overview.mdc)** - Tổng quan hệ thống Kiro
- **[Auto Task Execution](../../.cursor/rules/auto-task-execution.mdc)** - Thực thi task tự động

## ⚠️ CRITICAL ENFORCEMENT RULES

### Mandatory Compliance

1. **BẮT BUỘC** tuân thủ 100% các rules trong `.cursor/rules/`
2. **NGHIÊM CẤM** tạo rules mới trong `.trae/rules/` mà không sync với `.cursor/rules/`
3. **BẮT BUỘC** cập nhật alias links khi có thay đổi trong `.cursor/rules/`
4. **BẮT BUỘC** sử dụng relative paths để đảm bảo tính di động
5. **BẮT BUỘC** sử dụng Kiro Task Execution System cho mọi dự án
6. **BẮT BUỘC** kích hoạt Kiro Fallback Workflow khi thiếu file Kiro

### Synchronization Protocol

- Mọi thay đổi rules phải được thực hiện trong `.cursor/rules/` trước
- File này chỉ được cập nhật để sync alias links
- Không được override hoặc modify nội dung rules gốc

## 🔄 Rules Hierarchy Priority

1. **Kiro Task System** - Critical Priority (Must Execute)
2. `.cursor/rules/` - **PRIMARY SOURCE** (Highest Priority)
3. `.appdexer/rules/` - Secondary reference
4. `.trae/rules/` - Alias/Link layer only (Lowest Priority)

## Trae AI Specific Configuration

- **BẮT BUỘC** sử dụng rules từ `.cursor/rules/` làm nguồn chính thức
- File này chỉ chứa alias links và không được chứa rules độc lập
- Mọi customization phải được thực hiện trong `.cursor/rules/`

## 🎯 Kiro Task Execution System for Trae AI

### Core Features

- **Automatic Task Detection & Execution**: Tự động phát hiện và thực thi các Kiro tasks
- **Status Tracking**: Theo dõi trạng thái thực thi từng task
- **Fallback Workflow**: Quy trình dự phòng khi thiếu file Kiro
- **Quality Gates**: Cổng chất lượng tại mỗi giai đoạn

### Kiro Task Detection Algorithm

1. **Kiểm tra file Kiro**: `requirements.md`, `design.md`, `tasks.md`
2. **Phân tích trạng thái**: Xác định file nào thiếu hoặc không đầy đủ
3. **Kích hoạt workflow**: Primary flow hoặc Fallback flow
4. **Thực thi tasks**: Theo thứ tự ưu tiên và phụ thuộc

### Fallback Workflow Stages

#### 1. Brainstorm Stage
- Phân tích yêu cầu dự án từ context hiện có
- Tạo ý tưởng và định hướng phát triển
- Output: Brainstorm document với các ý tưởng chính

#### 2. Requirements Stage
- Chuyển đổi brainstorm thành requirements cụ thể
- Định nghĩa functional và non-functional requirements
- Output: `requirements.md` hoàn chỉnh

#### 3. Design Stage
- Thiết kế kiến trúc và UI/UX dựa trên requirements
- Tạo wireframes và system design
- Output: `design.md` với thiết kế chi tiết

#### 4. Tasks Stage
- Phân tích design và tạo danh sách tasks cụ thể
- Định nghĩa dependencies và priorities
- Output: `tasks.md` với action items

### Integration Benefits for Trae AI

- **Seamless Integration**: Tích hợp mượt mà với Trae AI workflow
- **Visual Task Tracking**: Theo dõi tasks trực quan trong IDE
- **Auto-completion**: Tự động hoàn thành tasks khi điều kiện đáp ứng
- **Error Prevention**: Ngăn ngừa lỗi thông qua quality gates
- **Dependency Resolution**: Tự động giải quyết phụ thuộc giữa tasks

## Android Development Workflow

### Blueprint-First Development

- **BẮT BUỘC** tạo blueprint trước khi viết code cho mỗi tính năng
- **BẮT BUỘC** kiểm tra module registry để tránh trùng lặp
- **BẮT BUỘC** cập nhật module registry sau khi hoàn thành tính năng
- **BẮT BUỘC** tuân thủ cấu trúc package tiêu chuẩn
- **BẮT BUỘC** sử dụng các base classes đã có
- **NGHIÊM CẤM** tạo code trùng lặp chức năng đã có

### Standard Package Structure

```
com.base.app/
├── base/                 # Base classes
│   ├── activity/         # Base Activities
│   ├── fragment/         # Base Fragments
│   ├── viewmodel/        # Base ViewModels
│   ├── adapter/          # Base Adapters
│   └── view/             # Base Custom Views
├── core/                 # Core modules
│   ├── di/               # Dependency Injection
│   ├── network/          # Network components
│   ├── storage/          # Local storage
│   ├── analytics/        # Analytics tracking
│   └── utils/            # Utility classes
├── data/                 # Data layer
│   ├── repository/       # Repositories implementation
│   ├── datasource/       # Data sources (remote, local)
│   ├── model/            # Data models (entities, DTOs)
│   └── mapper/           # Mappers
├── domain/               # Domain layer
│   ├── usecase/          # Use cases (business logic)
│   ├── model/            # Domain models
│   └── repository/       # Repository interfaces
└── ui/                   # UI layer
    ├── components/       # Shared UI components
    └── features/         # Feature packages
        ├── feature1/     # Tính năng 1
        ├── feature2/     # Tính năng 2
        └── ...
```

## Code Quality Standards

### Architecture

- Phân chia rõ ràng các layer (presentation, business logic, data)
- Sử dụng dependency injection để tách bạch các thành phần
- Ưu tiên composition over inheritance
- Thiết kế API dễ sử dụng và mở rộng
- Áp dụng Domain-Driven Design cho dự án phức tạp

### Security

- Validate tất cả input từ người dùng
- Sử dụng parameterized queries để tránh SQL injection
- Mã hoá dữ liệu nhạy cảm (passwords, tokens, PII)
- Implement đúng cách các authentication và authorization
- Tuân thủ hướng dẫn OWASP top 10
- Sử dụng HTTPS cho mọi API endpoints

### Performance

- Tối ưu database queries để tránh N+1 problems
- Implement caching cho dữ liệu tĩnh và truy vấn đắt
- Tránh blocking operations trong event loop
- Sử dụng pagination cho large data sets
- Lazy load components và modules khi có thể
- Profiling code để phát hiện bottlenecks

### Error Handling

- Xử lý tất cả exceptions và errors
- Cung cấp error messages hữu ích nhưng an toàn
- Log errors đúng cách với context đủ để debug
- Implement retry mechanisms cho unstable operations
- Sử dụng circuit breakers cho external services

### Testing

- Viết unit tests với test coverage cao
- Implement integration tests cho critical flows
- Sử dụng mocking để tách biệt dependencies
- Ưu tiên testing pyramids (nhiều unit tests, ít e2e tests)

## File Protection & Backup Rules

### Basic Principles

- **BẮT BUỘC** tạo backup trước khi xóa bất kỳ file hoặc thư mục nào
- **BẮT BUỘC** di chuyển file vào thư mục backup thay vì xóa trực tiếp
- **BẮT BUỘC** giữ cấu trúc thư mục khi backup để dễ dàng phục hồi sau này
- **BẮT BUỘC** ghi log mỗi khi di chuyển file vào backup
- **KHUYẾN NGHỊ** kiểm tra file trước khi xóa để đảm bảo không ảnh hưởng đến chức năng hiện có

### Backup Directory Structure

- Tạo thư mục `_backups` trong root của dự án (đã thêm vào .gitignore)
- Bên trong tạo cấu trúc theo dạng ngày: `_backups/YYYY-MM-DD/`
- Trong mỗi thư mục ngày, duy trì cấu trúc thư mục gốc để dễ dàng phục hồi
- Ví dụ: `src/components/Button.js` → `_backups/2024-05-10/src/components/Button.js`

### Backup Process

1. Xác định thời gian hiện tại để tạo thư mục backup nếu chưa tồn tại
2. Tạo thư mục cần thiết trong backup để giữ nguyên cấu trúc
3. Di chuyển file vào thư mục backup thay vì xóa trực tiếp
4. Cập nhật file log với thông tin: thời gian, đường dẫn gốc, lý do xóa
5. Thông báo cho người dùng về vị trí lưu backup

## Mockup Data Management

### Requirements

- Nếu dự án sử dụng bất kỳ dữ liệu giả lập nào, **BẮT BUỘC** tạo file MockupData.md
- Liệt kê chi tiết và cập nhật thường xuyên tất cả các phần của dự án đang sử dụng dữ liệu giả
- Phân loại dữ liệu giả lập theo mục đích sử dụng:
  - Dữ liệu demo cho client/stakeholders
  - Dữ liệu testing cho quá trình phát triển
  - Dữ liệu thay thế tạm thời cho API chưa sẵn sàng
  - Dữ liệu mẫu cho hướng dẫn/documentation

### Documentation Format

- Cho mỗi phần dữ liệu giả lập, ghi rõ:
  - Vị trí chính xác của file/code đang sử dụng dữ liệu giả
  - Cấu trúc dữ liệu của mockup và cấu trúc dữ liệu thật tương ứng
  - Phương thức khởi tạo và sử dụng dữ liệu giả
  - Kế hoạch và timeline để chuyển sang dữ liệu thật
  - Người chịu trách nhiệm cho việc thay thế dữ liệu giả

## Project Information

### Project Identity

- Luôn kiểm tra .project-identity để biết cấu trúc và ngôn ngữ dự án
- Nếu chưa có file .project-identity hãy tạo và yêu cầu người dùng bổ sung thêm thông tin

### Task Status Legend

- ✅ Completed
- ⏳ In Progress
- ❌ Not Started

## Git Workflow

### Commit Convention

- Tuân thủ quy ước commit (feat, fix, docs, style, refactor...)
- Sử dụng tiếng Anh cho commit messages
- Format: `type(scope): description`
- Examples:
  - `feat(camera): add new filter effects`
  - `fix(ui): resolve layout issue in preview`
  - `docs(readme): update installation guide`

### Branch Management

- Sử dụng feature branches cho mỗi tính năng mới
- Merge vào main branch sau khi review
- Xóa feature branches sau khi merge thành công

## Internationalization (i18n)

### String Resources

- Luôn kiểm tra và thêm các chuỗi dịch hoặc resource khi tạo
- Sử dụng tiếng Anh làm ngôn ngữ mặc định
- Hỗ trợ đa ngôn ngữ với tiếng Anh làm fallback
- Tạo string keys có ý nghĩa và dễ hiểu

### Resource Management

- Tổ chức strings theo feature hoặc screen
- Sử dụng plurals cho các string có số lượng
- Implement proper formatting cho dates, numbers, currencies

## Common Kiro System Commands

### Project Analysis

```bash
# Kiểm tra trạng thái Kiro system
kiro status

# Phân tích định dạng task
kiro analyze tasks
```

### Task Management

```bash
# Kiểm tra phụ thuộc task
kiro check dependencies

# Theo dõi tiến độ
kiro track progress

# Xác thực tiêu chí chấp nhận
kiro validate acceptance-criteria
```

## Getting Started with Trae AI + Kiro

1. **BẮT BUỘC**: Kiểm tra Kiro system khi bắt đầu dự án
2. **BẮT BUỘC**: Kích hoạt Kiro Fallback nếu thiếu file
3. Theo dõi task execution thông qua Trae AI interface
4. Sử dụng quality gates để đảm bảo chất lượng
5. Tận dụng auto-completion và dependency resolution
