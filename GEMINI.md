# Gemini IDE Project Rules

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng các rules từ `.cursor/rules/` làm nguồn chính thức cho tất cả workflows trong Gemini IDE.  
> File này chỉ là alias/link đến các rules chính thức trong `.cursor/rules/`

## 🎯 Primary Rules Sources (MANDATORY)

### Core Development Rules

- **[User Intent Analysis Workflow](./.cursor/rules/user-intent-analysis-workflow.mdc)** - 🔴 BẮT BUỘC: Phân tích ý định trước mọi hành động
- **[Base Rules](./.cursor/rules/base-rules.mdc)** - Quy tắc cơ bản cho tất cả projects
- **[Development Rules](./.cursor/rules/development-rules.mdc)** - Quy tắc phát triển chung
- **[Development Control Rules](./.cursor/rules/development-control-rules.mdc)** - Kiểm soát quy trình phát triển
- **[File Protection Rules](./.cursor/rules/file-protection-rules.mdc)** - Bảo vệ và backup files
- **[File Organization Rules](./.cursor/rules/file-organization-rules.mdc)** - Tổ chức file và thư mục

### Mobile Development Workflows

- **[Mobile Utility Workflow](./.cursor/rules/mobile-utility-workflow.mdc)** - Workflow chính cho mobile apps
- **[Android Workflow](./.cursor/rules/android-workflow.mdc)** - Quy trình phát triển Android
- **[iOS Workflow](./.cursor/rules/ios-workflow.mdc)** - Quy trình phát triển iOS
- **[TDD Mobile Workflow](./.cursor/rules/tdd-mobile-workflow.mdc)** - Test-driven development cho mobile

### Project Management

- **[Planning Workflow](./.cursor/rules/planning-workflow.mdc)** - Quy trình lập kế hoạch
- **[Planning Enforcement](./.cursor/rules/planning-enforcement.mdc)** - Thực thi kế hoạch
- **[Planning Validation Rules](./.cursor/rules/planning-validation-rules.mdc)** - Xác thực kế hoạch
- **[Project Identity Enforcement](./.cursor/rules/project-identity-enforcement.mdc)** - Kiểm tra định danh dự án
- **[Project Stage Manager](./.cursor/rules/project-stage-manager.mdc)** - Quản lý giai đoạn dự án

### Brainstorm & Expert System

- **[Brainstorm Overview](./.cursor/rules/brainstorm-overview.mdc)** - Tài liệu tổng quan về hệ thống brainstorm
- **[Brainstorm Workflow](./.cursor/rules/brainstorm-workflow.mdc)** - Quy trình brainstorm cơ bản
- **[Expert Brainstorm Workflow](./.cursor/rules/expert-brainstorm-workflow.mdc)** - Quy trình brainstorm với các chuyên gia
- **[Brainstorm Expert Integration](./.cursor/rules/brainstorm-expert-integration.mdc)** - Tích hợp quy trình brainstorm với nhiều chuyên gia
- **[Expert Brainstorm Guide](./.cursor/rules/expert-brainstorm-guide.mdc)** - Hướng dẫn thực hiện brainstorm với nhiều chuyên gia

### Code Quality & Architecture

- **[Android Code Deduplication](./.cursor/rules/android-code-deduplication.mdc)** - Tránh trùng lặp code Android
- **[Universal Code Deduplication](./.cursor/rules/universal-code-deduplication.mdc)** - Tránh trùng lặp code chung
- **[Android Project Template](./.cursor/rules/android-project-template.mdc)** - Template dự án Android
- **[iOS Project Template](./.cursor/rules/ios-project-template.mdc)** - Template dự án iOS
- **[TDD Guidelines](./.cursor/rules/tdd-guidelines.mdc)** - Hướng dẫn Test-Driven Development

### Integration & API

- **[API Integration Rules](./.cursor/rules/api-integration-rules.mdc)** - Quy tắc tích hợp API
- **[Backend Rules](./.cursor/rules/backend-rules-optimized.mdc)** - Quy tắc backend
- **[Frontend Rules](./.cursor/rules/frontend-rules.mdc)** - Quy tắc frontend
- **[Database Management](./.cursor/rules/database-management.mdc)** - Quản lý cơ sở dữ liệu

### Specialized Workflows

- **[Git Workflow](./.cursor/rules/git-workflow.mdc)** - Quy trình Git
- **[i18n Rules](./.cursor/rules/i18n-rules.mdc)** - Quốc tế hóa
- **[Resource Management](./.cursor/rules/resource-management.mdc)** - Quản lý tài nguyên
- **[Terminal Rules](./.cursor/rules/terminal-rules.mdc)** - Quy tắc terminal
- **[Design to Prompt Analysis](./.cursor/rules/design-to-prompt.mdc)** - Phân tích thiết kế và tạo prompt cho ứng dụng di động

### Project Setup & Identity

- **[Project Creation Workflow](./.cursor/rules/project-creation-workflow.mdc)** - Tạo dự án mới
- **[Project Identity Template](./.cursor/rules/project-identity-template.mdc)** - Template định danh dự án
- **[Project Identification Rules](./.cursor/rules/project-identification-rules.mdc)** - Nhận diện dự án
- **[Tech Stack Selection](./.cursor/rules/tech-stack-selection.mdc)** - Lựa chọn công nghệ
- **[Template Selection Workflow](./.cursor/rules/template-selection-workflow.mdc)** - Lựa chọn template

### Advanced Features

- **[Memory Bank Workflow](./.cursor/rules/memory-bank-workflow.mdc)** - Quản lý bộ nhớ
- **[Experience System Workflow](./.cursor/rules/experience-system-workflow.mdc)** - Hệ thống kinh nghiệm
- **[Context7 Auto Workflow](./.cursor/rules/context7-auto-workflow.mdc)** - Tự động kiểm tra context dự án
- **[Four Role Development](./.cursor/rules/four-role-development.mdc)** - Phát triển 4 vai trò

### Kiro Task Execution System

- **[Kiro Task Execution](./.cursor/rules/kiro-task-execution.mdc)** - 🔴 BẮT BUỘC: Hệ thống thực thi task tự động
- **[Kiro Fallback Workflow](./.cursor/rules/kiro-fallback-workflow.mdc)** - Quy trình dự phòng khi thiếu Kiro files
- **[Kiro System Overview](./.cursor/rules/kiro-system-overview.mdc)** - Tổng quan hệ thống Kiro
- **[Auto Task Execution](./.cursor/rules/auto-task-execution.mdc)** - Thực thi task tự động

## ⚠️ CRITICAL ENFORCEMENT RULES FOR GEMINI IDE

### Mandatory Compliance

1. **BẮT BUỘC** tuân thủ 100% các rules trong `.cursor/rules/`
2. **NGHIÊM CẤM** tạo rules mới mà không sync với `.cursor/rules/`
3. **BẮT BUỘC** sử dụng relative paths để đảm bảo tính di động
4. **BẮT BUỘC** kiểm tra `.project-identity` trước mọi task
5. **🔴 BẮT BUỘC** sử dụng Kiro Task Execution System cho mọi project
6. **🔴 BẮT BUỘC** kích hoạt Kiro Fallback Workflow khi thiếu files

### Gemini IDE Specific Configuration

- **BẮT BUỘC** sử dụng rules từ `.cursor/rules/` làm nguồn chính thức
- File này chỉ chứa alias links và không được chứa rules độc lập
- Mọi customization phải được thực hiện trong `.cursor/rules/`
- Tuân thủ cấu trúc project và workflow đã được định nghĩa

### Synchronization Protocol

- Mọi thay đổi rules phải được thực hiện trong `.cursor/rules/` trước
- File này chỉ được cập nhật để sync alias links
- Không được override hoặc modify nội dung rules gốc

## 🎯 Kiro Task Execution System for Gemini IDE

### Core Features

- **🔴 MANDATORY**: Automatic task detection từ `.kiro/specs/{project}/tasks.md`
- **🔴 MANDATORY**: Smart execution theo priority và dependencies
- **🔴 MANDATORY**: Real-time status tracking (pending, in-progress, completed, failed)
- **🔴 MANDATORY**: Fallback workflow khi thiếu Kiro files
- **🔴 MANDATORY**: Quality gates với acceptance criteria validation

### Kiro Task Detection Algorithm

```bash
# Gemini IDE sẽ tự động kiểm tra:
1. Kiểm tra .kiro/specs/{project}/tasks.md
2. Nếu thiếu → Kích hoạt Kiro Fallback Workflow
3. Nếu có → Parse và execute tasks theo priority
4. Update status real-time trong Gemini IDE
```

### Fallback Workflow Integration

1. **Brainstorm Stage**: Tạo insights từ user input
2. **Requirements Stage**: Generate structured requirements.md
3. **Design Stage**: Create technical design.md
4. **Tasks Stage**: Convert design thành Kiro tasks.md

### Gemini IDE Specific Benefits

- **Seamless Integration**: Tích hợp mượt mà với Gemini IDE workflow
- **Visual Task Tracking**: Hiển thị task progress trong IDE
- **Auto-completion**: Smart suggestions cho Kiro task format
- **Error Prevention**: Validate task format trước khi execute
- **Dependency Resolution**: Automatic task ordering trong IDE

## 🔄 Rules Hierarchy Priority

1. `.cursor/rules/` - **PRIMARY SOURCE** (Highest Priority)
2. **🔴 Kiro Task System** - Automatic execution layer (Critical Priority)
3. `.appdexer/rules/` - Secondary reference
4. `.trae/rules/` - Alias/Link layer
5. `GEMINI.md` - Gemini IDE specific alias (Lowest Priority)

## 🎯 Core Working Principles for Gemini IDE

### Communication Style

- Sử dụng tiếng Việt cho trò chuyện và giải thích với giọng điệu hài hước kiểu giới trẻ
- Trả lời rõ ràng, đầy đủ nhưng không dài dòng
- Luôn hỏi làm rõ khi yêu cầu không rõ ràng
- Thông báo khi bạn không chắc chắn về cách giải quyết

### Language

- Luôn kiểm tra và thêm các chuỗi dịch hoặc resource khi tạo
- Sử dụng tiếng Anh cho tất cả code và tài liệu kỹ thuật

### Core Working Principles

- **Luôn suy luận yêu cầu của người dùng trước khi thực hiện**
- Phân tích ý định thực sự đằng sau yêu cầu
- Luôn kiểm tra bộ nhớ để tìm thông tin liên quan
- Luôn phân tích kỹ trước khi chỉnh sửa code
- Tập trung vào vấn đề chính, không lạc đề
- Xác định rõ nguyên nhân gốc rễ (root cause) trước khi sửa lỗi
- Chỉ thực hiện một thay đổi lớn mỗi lần và kiểm tra kỹ trước khi tiếp tục

### 🧠 User Intent Analysis System (MANDATORY)

#### Core Principles for Gemini IDE

- **🔴 BẮT BUỘC: Phân tích ý định trước mọi hành động**
- **🔴 BẮT BUỘC: Không thực hiện ngay lập tức theo yêu cầu literal**
- **🔴 BẮT BUỘC: Luôn tìm hiểu mục tiêu thực sự đằng sau yêu cầu**
- **🔴 BẮT BUỘC: Đề xuất giải pháp tối ưu thay vì chỉ làm theo yêu cầu**
- **🔴 BẮT BUỘC: Xác nhận hiểu đúng ý định trước khi thực hiện**

#### 4-Phase Analysis Process

**Phase 1: Request Analysis**

- Phân tích yêu cầu chi tiết
- Xác định từ khóa và ngữ cảnh quan trọng
- Phát hiện các yêu cầu ngầm định
- Đánh giá độ phức tạp và tác động

**Phase 2: Context Gathering**

- Thu thập thông tin về trạng thái dự án hiện tại
- Đánh giá mức độ chuyên môn của người dùng
- Kiểm tra lịch sử và patterns trước đó
- Xác định các ràng buộc và giới hạn

**Phase 3: Intent Classification**

- **Feature Request**: Thêm tính năng mới
- **Bug Fix**: Sửa lỗi hoặc vấn đề
- **Optimization**: Cải thiện hiệu suất
- **Refactoring**: Tái cấu trúc code
- **Documentation**: Tạo/cập nhật tài liệu
- **Learning**: Học hỏi và hiểu biết
- **Exploration**: Khám phá và thử nghiệm

**Phase 4: Solution Generation**

- Tạo nhiều phương án giải quyết
- Đánh giá ưu nhược điểm của từng phương án
- Xác định phương án tối ưu
- Chuẩn bị presentation cho người dùng

#### Integration with Gemini IDE Workflows

- **🔴 BẮT BUỘC**: Sử dụng [User Intent Analysis Workflow](./.cursor/rules/user-intent-analysis-workflow.mdc)
- **🔴 BẮT BUỘC**: Tích hợp với Planning Workflow khi cần thiết
- **🔴 BẮT BUỘC**: Sử dụng Context7 để thu thập thông tin ngữ cảnh
- **🔴 BẮT BUỘC**: Cập nhật Memory Bank với patterns thành công

#### Quality Metrics for Gemini IDE

- **Intent Analysis Accuracy**: Target 95%
- **User Confirmation Rate**: Target 90%
- **Solution Optimality**: Target 85%
- **Rework Reduction**: Target 50%

### Problem Solving

- Ngừng ngay khi gặp vấn đề cần giải quyết
- Không nhảy vội vào việc sửa code khi gặp lỗi
- Luôn đưa ra 2-3 phương án khi giải quyết vấn đề phức tạp
- Dừng và xin hướng dẫn sau 3 lần thử không thành công

### Quality Standards

- Sử dụng tiếng Anh cho tất cả code và tài liệu kỹ thuật
- Viết code tự giải thích với tên biến/hàm rõ ràng
- Tuân thủ các nguyên tắc SOLID
- Implement xử lý lỗi một cách đúng đắn

### Documentation

- Ghi lại mọi quyết định quan trọng vào Decisions.md
- Cập nhật Codebase.md mỗi khi hoàn thiện một phần
- Đánh dấu các task đã hoàn thành trong Instruction.md
- Kết thúc mỗi nhiệm vụ với mô tả ngắn gọn về công việc đã làm

### Safety Measures

- Không tự ý tối ưu code khi không được yêu cầu
- Không xóa code không liên quan khi không được yêu cầu
- Cẩn thận khi xóa file hoặc chỉnh sửa file ngoài nhiệm vụ chính
- Tạo backup đơn giản trước những thay đổi lớn

## 📱 Android Development Workflow

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

## 🔧 Code Quality Standards

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

## 🛡️ File Protection & Backup Rules

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

## 📊 Mockup Data Management

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

## 📋 Project Information

### Project Identity

- Luôn kiểm tra .project-identity để biết cấu trúc và ngôn ngữ dự án
- Nếu chưa có file .project-identity hãy tạo và yêu cầu người dùng bổ sung thêm thông tin

### Task Status Legend

- ✅ Completed
- ⏳ In Progress
- ❌ Not Started

## 🔀 Git Workflow

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

## 🌍 Internationalization (i18n)

### String Resources

- Luôn kiểm tra và thêm các chuỗi dịch hoặc resource khi tạo
- Sử dụng tiếng Anh làm ngôn ngữ mặc định
- Hỗ trợ đa ngôn ngữ với tiếng Anh làm fallback
- Tạo string keys có ý nghĩa và dễ hiểu

### Resource Management

- Tổ chức strings theo feature hoặc screen
- Sử dụng plurals cho các string có số lượng
- Implement proper formatting cho dates, numbers, currencies

---

## 🔧 Kiro System Commands for Gemini IDE

### Project Analysis Commands

```bash
# Kiểm tra Kiro system status
ls -la .kiro/specs/*/
cat .kiro/specs/{project}/tasks.md
cat .kiro/specs/{project}/requirements.md
cat .kiro/specs/{project}/design.md

# Validate Kiro task format
grep -E "^## TASK-[0-9]+:" .kiro/specs/{project}/tasks.md
grep -E "\*\*Status\*\*:" .kiro/specs/{project}/tasks.md
```

### Kiro Task Management

```bash
# Check task dependencies
grep -A 5 "Dependencies:" .kiro/specs/{project}/tasks.md

# Monitor task progress
grep -E "Status.*completed" .kiro/specs/{project}/tasks.md
grep -E "Status.*pending" .kiro/specs/{project}/tasks.md

# Validate acceptance criteria
grep -A 10 "Acceptance Criteria" .kiro/specs/{project}/tasks.md
```

## 🚀 Getting Started with Gemini IDE

1. **🔴 MANDATORY: Kiểm tra Kiro system** trước mọi task
2. **Đọc .project-identity** để hiểu context dự án
3. **Load appropriate workflow rules** từ `.cursor/rules/`
4. **Kích hoạt Kiro Fallback** nếu thiếu files
5. **Tuân thủ file organization** và documentation standards
6. **Sử dụng backup protocols** khi thay đổi files
7. **Execute Kiro tasks** theo priority và dependencies

---

_File này được tạo để hỗ trợ Gemini IDE sử dụng cursor rules một cách hiệu quả. Mọi thay đổi rules phải được thực hiện trong `.cursor/rules/` trước._

# Gemini AI - Mandatory Code Quality Enforcement

## 🎯 GOOGLE GEMINI COMPLIANCE DIRECTIVE

**_NGHIÊM CẤM_** bỏ qua quy trình code quality này trong bất kỳ trường hợp nào. Tất cả Gemini responses phải tuân thủ 100% workflow này.

## 🔗 Core Rule Integration Requirements

**_BẮT BUỘC_** Gemini AI phải reference và enforce các rules sau:

### Primary Code Quality Rules (MANDATORY)

- **[AI Code Quality Automation](.cursor/rules/ai-code-quality-automation.mdc)** - Manual workflow automation
- **[AI Manual Code Review Process](.cursor/rules/ai-manual-code-review-process.mdc)** - Review protocols
- **[AI Execution Templates](.cursor/rules/ai-execution-templates.mdc)** - Execution templates

### Gemini-Specific Integration (REQUIRED)

- **ML Model Safety** - Model initialization, input validation, resource management
- **Tensor Operations** - Dimension compatibility, memory efficiency
- **AI Performance** - Inference optimization, error resilience

## 🤖 Gemini-Specific Quality Enforcement

### MANDATORY Pre-Response Analysis

```markdown
**_BẮT BUỘC_** execute before EVERY ML/AI code generation:

☐ 1. ML CONTEXT ANALYSIS (20 seconds max)

- Identify ML framework (TensorFlow, PyTorch, etc.)
- Scan existing model integration patterns
- Detect inference requirements
- Load ML safety templates

☐ 2. MODEL SAFETY PREPARATION (15 seconds max)

- Model initialization safety review
- Input validation template preparation
- Resource management setup
- Error handling pattern activation

☐ 3. CONFIDENCE THRESHOLD CONFIGURATION (5 seconds max)

- Model initialization fixes: 85% confidence
- Input validation fixes: 90% confidence
- Resource cleanup fixes: 85% confidence
- Performance optimization: 60% confidence
```

### MANDATORY Real-Time ML Code Validation

```markdown
**_BẮT BUỘC_** apply during EVERY ML code generation:

☐ 1. MODEL INITIALIZATION SAFETY (Real-time)

- Detect model loading → Auto-add error handling
- Detect configuration → Validate parameters
- Detect resource allocation → Add cleanup procedures
- Confidence threshold: 85%+

☐ 2. INPUT VALIDATION ENFORCEMENT (Real-time)

- Detect model input → Auto-add validation checks
- Detect tensor operations → Validate dimensions
- Detect data processing → Add type checking
- Confidence threshold: 90%+

☐ 3. RESOURCE MANAGEMENT VALIDATION (Real-time)

- Detect GPU usage → Add proper cleanup
- Detect memory allocation → Validate limits
- Detect model disposal → Ensure proper cleanup
- Confidence threshold: 85%+
```

### MANDATORY Post-ML Code Quality Assurance

```markdown
**_BẮT BUỘC_** execute after EVERY ML code completion:

☐ 1. MODEL LIFECYCLE VALIDATION (25 seconds max)

- Verify proper model initialization
- Confirm input validation coverage
- Validate error handling completeness
- Check resource cleanup procedures

☐ 2. PERFORMANCE OPTIMIZATION CHECK (20 seconds max)

- Scan for inference inefficiencies
- Validate batch processing usage
- Check for memory leaks
- Verify concurrent operation safety

☐ 3. ML SECURITY VALIDATION (15 seconds max)

- Scan for model file exposure
- Verify input sanitization
- Check for data privacy compliance
- Validate inference result safety
```

## 🚨 Gemini Critical Enforcement Rules

### Zero Tolerance Issues (BLOCK RESPONSE)

```markdown
**_NGHIÊM CẤM_** provide ML code with:

❌ Unsafe model initialization (no error handling)
❌ Missing input validation for model calls
❌ Unmanaged resource allocation
❌ Exposed model files or weights
❌ Memory leaks in inference operations

ACTION: Must auto-fix with 85%+ confidence or REFUSE to generate code
```

### High Priority Issues (FIX BEFORE RESPONSE)

```markdown
**_BẮT BUỘC_** resolve before providing ML code:

⚠️ Inefficient inference patterns
⚠️ Missing batch processing optimizations
⚠️ Incomplete error handling in ML operations
⚠️ Suboptimal memory usage patterns
⚠️ Missing model performance monitoring

ACTION: Auto-fix with 80%+ confidence or FLAG with explanation
```

## 📊 Gemini Quality Metrics & Monitoring

### ML Integration Metrics (MANDATORY)

```markdown
✅ Model initialization safety: > 90%
✅ Input validation coverage: > 95%
✅ Resource cleanup compliance: > 85%
✅ Error handling coverage: > 90%
✅ Performance optimization rate: > 80%
```

### Performance Requirements

```markdown
📈 ML code generation time: < 45 seconds
📈 Quality check execution time: < 60 seconds
📈 Auto-fix application rate: > 85%
📈 False positive rate: < 5%
📈 ML safety issue detection: > 90%
```

### Gemini-Specific Compliance Tracking

```markdown
🤖 Model safety pattern application: > 90%
🤖 Input validation robustness: > 95%
🤖 Resource management effectiveness: > 85%
🤖 Performance optimization rate: > 80%
🤖 ML security compliance: > 95%
```

## 🔄 Gemini Quality Monitoring System

### Real-Time ML Quality Gates

```json
{
  "ml_quality_gates": {
    "model_safety_check": {
      "enabled": true,
      "strictness": "high",
      "auto_fix": true,
      "confidence_threshold": 0.85
    },
    "input_validation_enforcement": {
      "enabled": true,
      "strictness": "very_high",
      "auto_fix": true,
      "confidence_threshold": 0.9
    },
    "resource_management_validation": {
      "enabled": true,
      "strictness": "high",
      "auto_fix": true,
      "confidence_threshold": 0.85
    },
    "performance_optimization": {
      "enabled": true,
      "strictness": "medium",
      "auto_fix": false,
      "suggest_improvements": true
    }
  }
}
```

### ML-Specific Escalation Matrix

```yaml
gemini_escalation_procedures:
  model_safety_violations:
    action: "block_code_generation"
    notification: "immediate_safety_flag"
    manual_intervention: "required"

  resource_management_issues:
    action: "auto_fix_with_validation"
    fallback: "provide_safe_alternatives"
    monitoring: "resource_usage_tracking"

  performance_inefficiencies:
    action: "suggest_optimizations"
    documentation: "include_performance_guidance"
    follow_up: "provide_benchmarking_tips"
```

## 🎯 Gemini ML Integration Templates

### Model Initialization Safety Templates

```python
# GEMINI AUTO-FIX: Safe Model Initialization
# BEFORE (UNSAFE):
model = GenerativeModel('gemini-pro')
response = model.generate_content(prompt)

# AFTER (SAFE - Auto-applied):
try:
    model = GenerativeModel('gemini-pro')

    # Validate model initialization
    if not model:
        raise ValueError("Failed to initialize Gemini model")

    # Add generation configuration for better control
    generation_config = GenerationConfig(
        temperature=0.7,
        top_p=0.8,
        top_k=40,
        max_output_tokens=1024,
    )

    response = model.generate_content(
        prompt,
        generation_config=generation_config
    )

    return response
except Exception as error:
    print(f"Model initialization failed: {error}")
    raise error
```

### Input Validation Templates

```python
# GEMINI AUTO-FIX: Input Validation Safety
# BEFORE (UNSAFE):
def process_with_ai(user_input):
    result = model.generate_content(user_input)
    return result.text

# AFTER (SAFE - Auto-applied):
def process_with_ai(user_input):
    try:
        # Input validation
        if not user_input or not isinstance(user_input, str):
            raise ValueError("Invalid input: must be non-empty string")

        # Length validation
        if len(user_input) > 10000:
            raise ValueError("Input too long: maximum 10,000 characters")

        # Content safety check (basic)
        if any(unsafe_pattern in user_input.lower() for unsafe_pattern in ['<script>', 'javascript:', 'data:']):
            raise ValueError("Potentially unsafe input detected")

        result = model.generate_content(user_input)

        # Validate response
        if not result or not result.text:
            raise ValueError("No valid response generated")

        return result.text
    except Exception as error:
        print(f"AI processing failed: {error}")
        return "Sorry, I couldn't process your request safely."
```

### Resource Management Templates

```python
# GEMINI AUTO-FIX: Resource Management
# BEFORE (RISKY):
def batch_process_data(data_list):
    results = []
    for data in data_list:
        result = model.generate_content(data)
        results.append(result.text)
    return results

# AFTER (SAFE - Auto-applied):
import time
from typing import List, Optional

def batch_process_data(data_list: List[str]) -> List[str]:
    results = []
    processed_count = 0

    try:
        for i, data in enumerate(data_list):
            try:
                # Input validation for each item
                if not data or len(data.strip()) == 0:
                    results.append("Empty input skipped")
                    continue

                # Rate limiting to prevent API overload
                if i > 0 and i % 10 == 0:
                    time.sleep(1)  # Brief pause every 10 requests

                result = model.generate_content(data)

                if result and result.text:
                    results.append(result.text)
                    processed_count += 1
                else:
                    results.append("No response generated")

            except Exception as item_error:
                print(f"Error processing item {i}: {item_error}")
                results.append(f"Error: {str(item_error)}")

        print(f"Successfully processed {processed_count}/{len(data_list)} items")
        return results

    except Exception as batch_error:
        print(f"Batch processing failed: {batch_error}")
        return [f"Batch error: {str(batch_error)}"] * len(data_list)
```

## 🚀 Gemini Implementation Guidelines

### ML Code Generation Protocol

```markdown
1. **PRE-ANALYSIS** (MANDATORY)

   - Execute ML safety workflow analysis
   - Load appropriate ML safety templates
   - Configure ML-specific confidence thresholds

2. **ML CODE GENERATION** (WITH REAL-TIME VALIDATION)

   - Apply safety patterns for model operations
   - Ensure input validation for all ML calls
   - Include appropriate resource management

3. **POST-VALIDATION** (MANDATORY)

   - Final ML safety and performance scan
   - Resource usage compliance verification
   - User education on ML best practices

4. **RESPONSE DELIVERY** (ENHANCED)
   - Include ML quality assurance summary
   - Highlight applied safety improvements
   - Provide ML performance optimization guidance
```

### ML-Specific User Education

```markdown
Gemini responses MUST include:

🧠 **ML Safety Improvements Applied:**

- [List of ML safety auto-fixes applied]
- [Reasoning for model safety enhancements]
- [Resource management optimizations]

⚡ **Performance Optimizations:**

- [Inference efficiency improvements]
- [Memory usage optimizations]
- [Batch processing recommendations]

🔒 **Security Considerations:**

- [Model security best practices]
- [Input validation importance]
- [Data privacy compliance notes]
```

## 📈 Continuous ML Improvement Framework

### ML-Specific Learning Mechanisms

```yaml
gemini_ml_learning_system:
  model_pattern_recognition:
    - successful_ml_patterns: "continuously_updated"
    - performance_optimization_tracking: "metrics_based"
    - safety_violation_prevention: "proactive"

  inference_optimization:
    - efficiency_pattern_detection: "performance_monitoring"
    - resource_usage_optimization: "automatic_tuning"
    - batch_processing_enhancement: "throughput_analysis"

  safety_enhancement:
    - ml_vulnerability_updates: "security_research_integration"
    - model_safety_evolution: "continuous_assessment"
    - privacy_compliance_tracking: "regulation_updates"
```

### ML Performance Tracking

```markdown
Weekly: ML safety pattern effectiveness analysis
Monthly: Model performance optimization review
Quarterly: ML security assessment and template updates
Annually: Complete ML workflow optimization
```

---

**GEMINI ENFORCEMENT STATUS**: 🔒 MANDATORY - ML SAFETY PRIORITY
**COMPLIANCE LEVEL**: 100% Required for all ML responses
**QUALITY GATES**: ML-specific automated checks
**PERFORMANCE MONITORING**: Real-time inference optimization
