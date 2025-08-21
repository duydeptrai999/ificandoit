# Info-Hub Integration for Trae AI

## Mandatory Info-Hub Check Protocol

### Pre-Work Requirements
- **BẮT BUỘC**: Kiểm tra `/info-hub.md` trước khi bắt đầu BẤT KỲ công việc nào
- **BẮT BUỘC**: Đọc phần "Currently Working" để tránh xung đột
- **BẮT BUỘC**: Đọc phần "Known Issues & Blockers" để biết vấn đề hiện tại
- **BẮT BUỘC**: Kiểm tra "Task Queue" để hiểu priority

### Work Declaration Protocol

#### Khi Bắt Đầu Công Việc
1. Cập nhật bảng "Currently Working" trong info-hub.md:
   ```
   | Trae | [Mô tả ý định chi tiết] | [Files sẽ chỉnh sửa] | In Progress | [Timestamp] |
   ```

2. Format mô tả ý định:
   - Rõ ràng, cụ thể về mục tiêu
   - Bao gồm scope của công việc
   - Ước tính thời gian hoàn thành

3. Target Files:
   - Ban đầu có thể để "TBD" nếu chưa xác định
   - Cập nhật ngay khi tìm được file cụ thể
   - Liệt kê đầy đủ tất cả files sẽ chỉnh sửa

#### Trong Quá Trình Làm Việc
- **BẮT BUỘC**: Cập nhật "Target Files" khi tìm được file cụ thể
- **BẮT BUỘC**: Kiểm tra xung đột trước khi chỉnh sửa file
- **NGHIÊM CẤM**: Chỉnh sửa file đang được AI khác làm việc

#### Sau Khi Hoàn Thành
- **BẮT BUỘC**: Xóa dòng khỏi bảng "Currently Working"
- **BẮT BUỘC**: Cập nhật "Recent Changes" nếu là thay đổi quan trọng
- **BẮT BUỘC**: Cập nhật timestamp "Last Updated" ở đầu file

### Lock File Protocol
- Tạo `.info-hub.lock` khi cập nhật info-hub.md
- Xóa lock file sau khi hoàn thành cập nhật
- Chờ nếu lock file đã tồn tại (AI khác đang cập nhật)

### Error Handling
- Nếu không thể truy cập info-hub.md: Thông báo lỗi và dừng
- Nếu phát hiện xung đột: Thông báo và yêu cầu user quyết định
- Nếu lock file tồn tại quá 5 phút: Cảnh báo và hỏi user

### Integration với Trae Workflows
- Tích hợp check info-hub vào tất cả Trae workflows
- Ưu tiên info-hub check trước Context7 check
- Đảm bảo compatibility với existing Trae rules

## Example Usage

### Scenario 1: Feature Development
```
| Trae | Implementing user authentication module | auth/login.js, auth/register.js, auth/middleware.js | In Progress | 2024-12-20 11:00 |
```

### Scenario 2: Bug Fix
```
| Trae | Fixing payment gateway timeout issue | payment/gateway.js, config/timeout.js | In Progress | 2024-12-20 11:15 |
```

### Scenario 3: Documentation Update
```
| Trae | Updating API documentation for v2.0 | docs/api-v2.md, README.md | In Progress | 2024-12-20 11:30 |
```

## Compliance Verification
- **BẮT BUỘC**: Trae AI phải verify compliance với protocol này
- **BẮT BUỘC**: Log mọi info-hub interactions để audit
- **BẮT BUỘC**: Report violations để continuous improvement