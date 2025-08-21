# Info-Hub Integration for Kiro System

## Mandatory Info-Hub Check Protocol for Kiro

### Pre-Task Execution Requirements
- **BẮT BUỘC**: Kiểm tra `/info-hub.md` trước khi thực thi BẤT KỲ Kiro task nào
- **BẮT BUỘC**: Validate không có AI nào đang làm việc trên cùng specs/tasks
- **BẮT BUỘC**: Kiểm tra "Known Issues & Blockers" cho Kiro-specific problems
- **BẮT BUỘC**: Sync với "Task Queue" để avoid duplication

### Kiro Task Declaration Protocol

#### Khi Bắt Đầu Task Execution
1. Cập nhật bảng "Currently Working" trong info-hub.md:
   ```
   | Kiro | [Task type và mô tả] | [Spec files và output files] | In Progress | [Timestamp] |
   ```

2. Kiro-specific Intent Format:
   - **Task Type**: CREATE_NEW, UPDATE_EXISTING, SUPPLEMENT_DATA, RESTRUCTURE
   - **Scope**: Requirements, Design, Tasks, hoặc Full Workflow
   - **Target Project**: Tên project trong .kiro/specs/
   - **Expected Output**: Files sẽ được tạo/cập nhật

3. Target Files cho Kiro:
   - `.kiro/specs/{project}/requirements.md`
   - `.kiro/specs/{project}/design.md`
   - `.kiro/specs/{project}/tasks.md`
   - Các files liên quan khác

#### Kiro Dynamic Workflow Integration
- **BẮT BUỘC**: Declare intent cho từng stage của Dynamic Workflow
- **BẮT BUỘC**: Update Target Files khi chuyển stage
- **BẮT BUỘC**: Maintain consistency với Kiro Task Execution rules

#### Stage-Specific Declarations

##### Brainstorm Stage
```
| Kiro | Brainstorm stage for [project] - analyzing requirements and generating ideas | brainstorms/[project].md | In Progress | [Timestamp] |
```

##### Requirements Stage
```
| Kiro | Requirements stage for [project] - converting brainstorm to functional requirements | .kiro/specs/[project]/requirements.md | In Progress | [Timestamp] |
```

##### Design Stage
```
| Kiro | Design stage for [project] - creating architecture and UI/UX design | .kiro/specs/[project]/design.md | In Progress | [Timestamp] |
```

##### Tasks Stage
```
| Kiro | Tasks stage for [project] - generating actionable development tasks | .kiro/specs/[project]/tasks.md | In Progress | [Timestamp] |
```

### Kiro-Specific Conflict Resolution
- **NGHIÊM CẤM**: Thực thi task nếu AI khác đang làm việc trên cùng project specs
- **BẮT BUỘC**: Coordinate với other AIs khi cần access shared resources
- **BẮT BUỘC**: Respect file locks và wait for completion

### Quality Gates Integration
- **BẮT BUỘC**: Update info-hub khi pass/fail quality gates
- **BẮT BUỘC**: Document quality issues trong "Known Issues & Blockers"
- **BẮT BUỘC**: Notify other AIs về quality gate status

### Auto-Task Execution Coordination
- **BẮT BUỘC**: Declare intent trước khi auto-execute tasks
- **BẮT BUỘC**: Update progress cho long-running auto-executions
- **BẮT BUỘC**: Handle interruptions gracefully

### Kiro System Status Reporting
- **BẮT BUỘC**: Report Kiro system health trong info-hub
- **BẮT BUỘC**: Update metrics section với Kiro performance data
- **BẮT BUỘC**: Log Kiro-specific errors và resolutions

## Example Kiro Declarations

### Full Project Creation
```
| Kiro | Creating complete specs for e-commerce mobile app | .kiro/specs/ecommerce-app/requirements.md, design.md, tasks.md | In Progress | 2024-12-20 11:00 |
```

### Requirements Update
```
| Kiro | Updating requirements for payment integration | .kiro/specs/ecommerce-app/requirements.md | In Progress | 2024-12-20 11:15 |
```

### Task Generation
```
| Kiro | Generating development tasks for user authentication | .kiro/specs/ecommerce-app/tasks.md | In Progress | 2024-12-20 11:30 |
```

### Multi-Stage Workflow
```
| Kiro | Dynamic workflow: Brainstorm → Requirements → Design → Tasks for CRM system | brainstorms/crm-system.md, .kiro/specs/crm-system/* | In Progress | 2024-12-20 11:45 |
```

## Integration với Existing Kiro Rules
- Tuân thủ tất cả rules trong `.cursor/rules/kiro-*.mdc`
- Maintain compatibility với Kiro Task Execution System
- Enhance existing workflows với info-hub coordination
- Preserve Kiro's autonomous operation capabilities

## Monitoring và Metrics
- Track Kiro task completion rates
- Monitor conflict resolution effectiveness
- Measure coordination overhead
- Report performance impact của info-hub integration