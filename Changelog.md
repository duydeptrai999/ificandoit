# Changelog

All notable changes to this project will be documented in this file.

## [Latest] - 2024-12-19

### Fix Photo Adjustment on Filtered Images
**Yêu cầu**: Sửa lỗi khi áp dụng adjustment trên ảnh đã có filter, adjustment bị áp dụng lên ảnh gốc thay vì ảnh đã có filter

**Vấn đề**: PhotoAdjustView nhận rawPhotoBitmap (ảnh gốc) làm input thay vì currentBitmap (ảnh đã có filter), khiến các điều chỉnh bị áp dụng lên ảnh gốc và filter bị mất

**Giải pháp**: 
- Thay đổi parameter `originalBitmap` trong PhotoAdjustView từ `rawPhotoBitmap` thành `currentBitmap`
- Điều này đảm bảo adjustment được áp dụng lên ảnh đã có filter thay vì ảnh gốc

**Kết quả**: User có thể áp dụng filter trước, sau đó điều chỉnh (adjust) mà không bị mất filter đã áp dụng

---

### Photo Adjust Panel Height Increase
**Yêu cầu**: Tăng chiều cao panel điều chỉnh ảnh để chiếm nhiều diện tích hơn

**Thực hiện**:
- Tăng weight của AdjustmentControlsPanel từ 1.2f lên 1.5f để panel chiếm nhiều diện tích hơn
- Giảm padding ngang và dọc của nội dung cuộn được từ (12.dp, 6.dp) xuống (8.dp, 4.dp)
- Giảm padding dưới của tiêu đề "Basic adjustments" và "Advanced adjustments" từ 12.dp xuống 8.dp
- Giảm chiều cao của Spacer giữa các phần điều chỉnh từ 16.dp xuống 8.dp
- Giảm chiều cao của Spacer cuối cùng từ 12.dp xuống 8.dp

**Kết quả**: Panel điều chỉnh ảnh hiện chiếm nhiều diện tích hơn với layout được tối ưu hóa.

---

### Major Photo Adjustment Panel Redesign
**Yêu cầu**: Điều chỉnh lại giao diện điều chỉnh ảnh để hiển thị đầy đủ các thanh điều chỉnh và loại bỏ phần top bar để tăng chiều cao tổng thể.

**Thực hiện**:
- **Loại bỏ AdjustTopBar**: Xóa phần top bar chứa "cancel", "adjust", "reset", "apply" để tăng không gian hiển thị
- **Tích hợp controls vào panel**: Di chuyển các nút điều khiển xuống cuối panel dưới dạng Row cố định
- **Tối ưu layout structure**:
  - Sử dụng Column với weight(1f) cho scrollable content
  - Row cố định ở dưới chứa 3 nút: Cancel, Reset, Apply
  - Panel chiếm toàn bộ chiều cao còn lại (weight = 1f)
- **Cải thiện hiển thị slider**:
  - Đảm bảo tất cả slider (Basic và Advanced) hiển thị đầy đủ
  - Tối ưu padding và spacing để tận dụng không gian
  - Giữ nguyên các cải tiến UI/UX trước đó (icon 20dp, typography cải thiện, slider 48dp)

**Kết quả**: Panel điều chỉnh ảnh hiện hiển thị đầy đủ tất cả các thanh trượt với không gian làm việc tối đa, giao diện gọn gàng và trực quan hơn.

---

## [Unreleased]

### Added
- **Photo Adjustment Feature**: Tích hợp hoàn chỉnh tính năng điều chỉnh ảnh vào PhotoPreviewScreen
  - Thêm PhotoAdjustView component với giao diện tối ưu hóa
  - Thiết kế split-screen: preview ảnh (3/4) + điều khiển (1/4)
  - Hỗ trợ điều chỉnh cơ bản: Brightness, Contrast, Saturation
  - Hỗ trợ điều chỉnh nâng cao: Highlights, Shadows, Warmth, Tint
  - Tích hợp vào PhotoPreviewScreen với state management
  - Thêm string resources để hỗ trợ đa ngôn ngữ
  - Xem trước thời gian thực trên ảnh chính khi điều chỉnh
  - Chức năng Apply/Cancel để xác nhận hoặc hủy bỏ thay đổi
  - Linear progress indicator cho quá trình xử lý
  - Hệ thống callback preview trực tiếp cho phản hồi tức thì - 2024-12-19

### Improved
- **Enhanced UI/UX for Photo Adjustment (Major Update)**
  - **Expanded Item Design**: Increased adjustment item sizes for better usability
    - Increased icon size from 16dp to 20dp for better visibility
    - Enhanced text size from bodySmall to bodyMedium for labels
    - Changed value text from labelSmall to bodySmall for readability
    - Increased vertical padding from 4dp to 8dp for easier touch
    - Increased spacing between icon and text from 6dp to 8dp
  - **Significantly Increased Panel Height**: Enhanced adjustment panel height (weight from 1.3f to 1.8f)
  - **Improved Padding**: Optimized panel padding to 16dp horizontal, 8dp vertical
  - **Enhanced Slider Control**: Increased slider height to 48dp for better touch control
  - **Improved Typography**: Increased title size from titleSmall to titleMedium
  - Cải thiện trải nghiệm người dùng với preview ảnh luôn hiển thị
  - Tối ưu hóa layout mobile cho khả năng sử dụng tốt hơn
  - Loại bỏ preview ảnh thừa trong panel điều chỉnh
  - Phản hồi trực quan tốt hơn trong quá trình xử lý

### ✅ Fixed Camera Session Crash - Safe Back Navigation with Confirmation Dialog

**Yêu cầu**: Giải quyết lỗi crash app khi swipe back từ màn hình PhotoPreviewScreen với thông báo "Session has been closed; further changes are illegal". Thêm dialog xác nhận khi người dùng muốn hủy bỏ thay đổi.

**Vấn đề gốc**:
- App crash với lỗi camera session khi navigate back từ PhotoPreviewScreen
- Người dùng có thể vô tình mất công sức chỉnh sửa ảnh
- Không có cảnh báo khi hủy bỏ thay đổi

**Cách triển khai**:
1. **BackHandler Integration**: Thêm `BackHandler` trong PhotoPreviewScreen để intercept back gesture
2. **Confirmation Dialog**: Tạo `DiscardChangesDialog` để xác nhận hành động hủy bỏ
3. **Safe Camera Cleanup**: Thêm `DisposableEffect` trong CameraScreen để cleanup camera resources
4. **Error Handling**: Xử lý lỗi trong quá trình cleanup mà không crash app

**Kết quả đạt được**:
- ✅ Không còn crash app khi swipe back từ PhotoPreviewScreen
- ✅ Dialog xác nhận xuất hiện khi người dùng muốn hủy bỏ thay đổi
- ✅ Camera session được đóng an toàn khi navigate away
- ✅ Người dùng có thể chọn tiếp tục chỉnh sửa hoặc hủy bỏ
- ✅ Improved UX với safe navigation pattern

**Files đã chỉnh sửa**:
- `PhotoPreviewScreen.kt`: Thêm BackHandler và DiscardChangesDialog
- `CameraScreen.kt`: Thêm DisposableEffect cho camera cleanup
- `strings.xml`: Thêm string resources cho dialog
- `Help.md`: Cập nhật documentation

---

## [Previous] - 2024-12-19

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