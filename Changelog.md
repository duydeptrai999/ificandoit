# Changelog

All notable changes to this project will be documented in this file.

## [Latest] - 2024-12-19

### ✅ Enhanced Photo Crop Feature - Smart Aspect Ratio Application

**Yêu cầu**: Cải tiến tính năng Crop để khi chọn tỷ lệ khung hình, khung cắt sẽ áp dụng tỷ lệ ngay lập tức, nhưng sau đó người dùng vẫn có thể điều chỉnh kích thước một cách độc lập, không bị ràng buộc bởi tỷ lệ đã chọn.

**Cách triển khai**:
1. **Simplified Logic**: Loại bỏ logic phức tạp về khóa/mở khóa tỷ lệ
2. **Smart Application**: Tạo hàm `applyCropAspectRatio()` để áp dụng tỷ lệ ngay khi chọn
3. **Free Form Dragging**: Tất cả thao tác kéo đều sử dụng logic tự do, không bị ràng buộc tỷ lệ
4. **UI Simplification**: Loại bỏ nút toggle phức tạp, workflow đơn giản hơn

**Kết quả đạt được**:
- Chọn tỷ lệ khung hình → áp dụng tỷ lệ ngay lập tức vào khung cắt
- Sau đó có thể kéo để điều chỉnh chiều dài, rộng độc lập
- Có thể phá vỡ tỷ lệ ban đầu để tạo kích thước tùy ý
- Workflow trực quan và dễ hiểu: chọn → áp dụng → điều chỉnh tự do
- Loại bỏ sự phức tạp của nút khóa/mở khóa

**Files đã chỉnh sửa**:
- `PhotoCropView.kt`: Đơn giản hóa logic, thêm hàm `applyCropAspectRatio()`
- `Help.md`: Cập nhật hướng dẫn sử dụng theo workflow mới
- `Changelog.md`: Ghi lại thay đổi

---

## [Previous] - 2024-12-19

### ✅ Added
- **Tính năng Crop Ảnh trong PhotoPreviewScreen**
  - Tạo component PhotoCropView với khả năng cắt ảnh tương tác
  - Hỗ trợ kéo thả để chọn vùng cắt (góc, cạnh, di chuyển)
  - Cung cấp các tỷ lệ khung hình: Free, 1:1, 4:3, 16:9, 3:4, 9:16
  - Tích hợp vào PhotoPreviewScreen với UI/UX mượt mà
  - Thêm string resources cho đa ngôn ngữ
  - Xử lý logic crop bitmap chính xác

### 🔄 Improved
- **Cải tiến UI Crop Screen**
  - Thay thế Row bằng LazyRow cho thanh chọn tỷ lệ khung hình
  - Hỗ trợ scroll ngang khi có nhiều tùy chọn tỷ lệ
  - Giải quyết vấn đề hiển thị không đủ diện tích cho tỷ lệ 9:16
  - Thêm spacing và padding phù hợp cho trải nghiệm tốt hơn

### 🔧 Technical Implementation
- Sử dụng Jetpack Compose Canvas cho rendering
- Gesture handling với detectDragGestures
- State management với remember và mutableStateOf
- Tính toán tỷ lệ và kích thước động
- Error handling và validation

### 📁 Files Modified/Created
- `PhotoCropView.kt` - Component chính cho tính năng crop
- `PhotoPreviewScreen.kt` - Tích hợp tính năng crop
- `strings.xml` - Thêm string resources
- `Help.md` - Tài liệu hướng dẫn sử dụng

### 🎯 User Experience
- Interface trực quan và dễ sử dụng
- Phản hồi tức thì khi tương tác
- Hỗ trợ nhiều tỷ lệ khung hình phổ biến
- Tích hợp mượt mà với workflow chụp ảnh hiện có