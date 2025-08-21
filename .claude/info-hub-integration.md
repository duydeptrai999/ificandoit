# Info-Hub Integration for Claude AI

## Mandatory Info-Hub Check Protocol for Claude

### Pre-Work Requirements
- **BẮT BUỘC**: Kiểm tra `/info-hub.md` trước khi bắt đầu BẤT KỲ công việc nào
- **BẮT BUỘC**: Đọc và hiểu current project context từ info-hub
- **BẮT BUỘC**: Kiểm tra conflicts với other AI tools
- **BẮT BUỘC**: Validate access permissions cho target files

### Claude Work Declaration Protocol

#### Khi Bắt Đầu Session
1. Cập nhật bảng "Currently Working" trong info-hub.md:
   ```
   | Claude | [Detailed work intent] | [Specific files to modify] | In Progress | [Timestamp] |
   ```

2. Claude-specific Intent Format:
   - **Work Type**: Analysis, Implementation, Debugging, Documentation, Refactoring
   - **Scope**: Component-level, Module-level, System-level
   - **Complexity**: Simple, Medium, Complex
   - **Estimated Duration**: Short (<30min), Medium (30min-2h), Long (>2h)

3. Target Files Declaration:
   - List all files that will be modified
   - Include new files that will be created
   - Specify directories that will be affected
   - Update list as work progresses

#### Claude-Specific Work Categories

##### Code Analysis & Review
```
| Claude | Analyzing codebase architecture for performance optimization | src/components/*, config/performance.js | In Progress | [Timestamp] |
```

##### Feature Implementation
```
| Claude | Implementing user dashboard with real-time notifications | src/dashboard/*, src/notifications/* | In Progress | [Timestamp] |
```

##### Bug Investigation & Fix
```
| Claude | Debugging memory leak in data processing module | src/data/processor.js, tests/data-tests.js | In Progress | [Timestamp] |
```

##### Documentation & Specs
```
| Claude | Creating API documentation for v3.0 endpoints | docs/api-v3.md, README.md | In Progress | [Timestamp] |
```

##### System Architecture
```
| Claude | Designing microservices architecture for scalability | docs/architecture.md, config/services.yaml | In Progress | [Timestamp] |
```

### Claude Collaboration Protocol

#### With Cursor IDE
- **BẮT BUỘC**: Coordinate với Cursor cho real-time editing
- **BẮT BUỘC**: Respect Cursor's active editing sessions
- **BẮT BUỘC**: Sync changes để avoid conflicts

#### With Trae AI
- **BẮT BUỘC**: Coordinate cho mobile development tasks
- **BẮT BUỘC**: Share context về mobile-specific requirements
- **BẮT BUỘC**: Avoid duplicate mobile implementations

#### With Kiro System
- **BẮT BUỘC**: Respect Kiro's spec generation processes
- **BẮT BUỘC**: Coordinate task execution với Kiro workflows
- **BẮT BUỘC**: Maintain consistency với Kiro-generated specs

#### With Gemini
- **BẮT BUỘC**: Share analysis results và insights
- **BẮT BUỘC**: Coordinate research và documentation tasks
- **BẮT BUỘC**: Avoid duplicate analytical work

### Claude Quality Assurance

#### Pre-Implementation Checks
- Verify no conflicts với ongoing work
- Validate technical approach với project standards
- Confirm resource availability
- Check dependencies và prerequisites

#### During Implementation
- Update progress trong info-hub regularly
- Document significant decisions
- Report blockers immediately
- Maintain code quality standards

#### Post-Implementation
- Update "Recent Changes" section
- Document lessons learned
- Clean up work status từ info-hub
- Prepare handoff documentation

### Claude Error Handling & Recovery

#### Conflict Detection
- **BẮT BUỘC**: Stop work nếu detect file conflicts
- **BẮT BUỘC**: Notify user về conflict situation
- **BẮT BUỘC**: Propose resolution strategies

#### System Errors
- **BẮT BUỘC**: Log errors trong "Known Issues & Blockers"
- **BẮT BUỘC**: Provide detailed error context
- **BẮT BUỘC**: Suggest recovery actions

#### Communication Failures
- **BẮT BUỘC**: Implement retry mechanisms
- **BẮT BUỘC**: Fallback to manual coordination
- **BẮT BUỘC**: Document communication issues

### Claude Performance Optimization

#### Efficient Info-Hub Usage
- Cache info-hub content for session duration
- Minimize read/write operations
- Batch updates when possible
- Use incremental updates

#### Resource Management
- Monitor memory usage during large tasks
- Implement progress checkpoints
- Optimize file I/O operations
- Manage concurrent operations

## Example Claude Work Declarations

### Complex Feature Development
```
| Claude | Implementing advanced search with AI-powered recommendations | src/search/*, src/ai/recommendations.js, src/api/search-endpoints.js | In Progress | 2024-12-20 11:00 |
```

### System Integration
```
| Claude | Integrating third-party payment gateway with error handling | src/payments/gateway.js, src/payments/error-handler.js, tests/payment-tests.js | In Progress | 2024-12-20 11:15 |
```

### Performance Optimization
```
| Claude | Optimizing database queries and implementing caching layer | src/database/queries.js, src/cache/redis-client.js, config/database.js | In Progress | 2024-12-20 11:30 |
```

### Architecture Refactoring
```
| Claude | Refactoring monolith to microservices architecture | src/services/*, config/microservices.yaml, docs/migration-plan.md | In Progress | 2024-12-20 11:45 |
```

## Integration với Claude Workflows
- Enhance existing Claude capabilities với coordination
- Maintain Claude's analytical strengths
- Preserve Claude's code quality focus
- Enable seamless multi-AI collaboration

## Monitoring và Success Metrics
- Track collaboration effectiveness
- Measure conflict reduction
- Monitor code quality maintenance
- Assess user satisfaction với multi-AI coordination