# Help - Hướng dẫn sử dụng các tính năng

## Tính năng Điều chỉnh Ảnh

### Mô tả
Tính năng điều chỉnh ảnh cho phép người dùng chỉnh sửa các thông số của ảnh như độ sáng, độ tương phản, độ bão hòa, và nhiều thông số khác.

### Thành phần chính
- **PhotoAdjustView**: Component chính để điều chỉnh ảnh
- **AdjustmentValues**: Data class chứa các giá trị điều chỉnh
- **PhotoUtils**: Utility class để áp dụng các điều chỉnh lên bitmap

### Thiết kế UI/UX
- **Layout tối ưu**: Loại bỏ top bar, panel chiếm toàn bộ chiều cao (weight 1f)
- **Controls integration**: Nút điều khiển (Cancel, Reset, Apply) được tích hợp ở cuối panel
- **Scrollable content**: Phần điều chỉnh có thể cuộn để hiển thị đầy đủ tất cả slider
- **Xem trước thời gian thực**: Điều chỉnh được áp dụng ngay lập tức lên ảnh chính
- **Giao diện tối ưu cho mobile**: Thiết kế với các điều khiển dễ sử dụng trên thiết bị di động
- **Kích thước item tối ưu**: Icon 20dp, typography cải thiện (bodyMedium cho nhãn, bodySmall Bold cho giá trị)
- **Padding tối ưu**: Horizontal 16dp, vertical 8dp để tận dụng tối đa không gian
- **Slider height**: 48dp cho trải nghiệm touch tốt hơn
- **Spacing cân bằng**: Khoảng cách icon-text 8dp, padding dọc slider 8dp
- **Typography cải thiện**: Tiêu đề từ titleSmall lên titleMedium

### Tính năng
- **Điều chỉnh cơ bản**: Độ sáng, độ tương phản, độ bão hòa
- **Điều chỉnh nâng cao**: Highlights, Shadows, Warmth, Tint
- **Xem trước trực tiếp**: Xem thay đổi ngay lập tức trên ảnh chính khi kéo slider
- **Chức năng Reset**: Khôi phục về trạng thái ban đầu
- **Apply/Cancel**: Áp dụng hoặc hủy bỏ các thay đổi
- **Thanh tiến trình**: Hiển thị khi đang xử lý điều chỉnh
- **Bố cục tối ưu**: Ảnh luôn hiển thị trong khi điều chỉnh để có trải nghiệm tốt hơn

### Cách sử dụng
1. Trong PhotoPreviewScreen, chọn tùy chọn "Adjust" từ bottom bar
2. Màn hình chia đôi: ảnh xem trước ở trên (3/4), bảng điều khiển ở dưới (1/4)
3. Kéo các slider để điều chỉnh - thay đổi hiển thị ngay lập tức trên ảnh chính
4. Cuộn trong bảng điều chỉnh để truy cập tất cả các điều khiển
5. Nhấn "Apply" để áp dụng hoặc "Cancel" để hủy bỏ

### Tích hợp
- Tích hợp vào PhotoPreviewScreen thông qua state management
- Sử dụng string resources để hỗ trợ đa ngôn ngữ
- Tuân thủ Material Design 3 guidelines

### Files liên quan
- `PhotoPreviewScreen.kt`: Màn hình chính chứa tính năng điều chỉnh
- `PhotoAdjustView.kt`: Component xử lý logic điều chỉnh ảnh
- `AdjustmentValues.kt`: Data class cho các giá trị điều chỉnh
- `PhotoUtils.kt`: Utility functions cho xử lý ảnh
- `strings.xml`: Chứa các chuỗi text cho UI

## Tính năng Crop Ảnh

### Mô tả
Tính năng crop ảnh cho phép người dùng cắt và điều chỉnh kích thước ảnh sau khi chụp trong `PhotoPreviewScreen`. Người dùng có thể chọn tỷ lệ khung hình để áp dụng ngay lập tức, sau đó tự do điều chỉnh kích thước mà không bị ràng buộc bởi tỷ lệ đã chọn.

### Cách hoạt động
1. **Truy cập**: Từ màn hình preview ảnh, nhấn nút "Crop" để mở giao diện crop
2. **Chọn tỷ lệ khung hình**: Sử dụng thanh tỷ lệ ở dưới để chọn tỷ lệ mong muốn
3. **Áp dụng tỷ lệ tự động**: Khung crop sẽ tự động điều chỉnh theo tỷ lệ đã chọn ngay lập tức
4. **Điều chỉnh tự do**: 
   - Kéo các góc hoặc cạnh để thay đổi kích thước vùng crop
   - Có thể điều chỉnh chiều dài và rộng độc lập, không bị ràng buộc tỷ lệ
   - Phá vỡ tỷ lệ ban đầu để tạo kích thước tùy ý
5. **Di chuyển khung**: Nhấn và kéo bên trong khung để di chuyển toàn bộ vùng crop
6. **Hoàn thành**: Nhấn "Done" để áp dụng hoặc "Cancel" để hủy

### Các thành phần chính
- **PhotoCropView**: Component chính xử lý giao diện crop
- **CropTopBar**: Thanh công cụ trên cùng với nút Cancel/Done
- **CropBottomBar**: Thanh chọn tỷ lệ khung hình với cuộn ngang
- **Interactive Canvas**: Vùng vẽ tương tác cho việc crop
- **applyCropAspectRatio()**: Hàm áp dụng tỷ lệ vào khung crop hiện tại

### Tỷ lệ khung hình hỗ trợ
- **Free**: Tự do điều chỉnh không theo tỷ lệ cố định
- **1:1**: Hình vuông
- **4:3**: Tỷ lệ landscape truyền thống
- **16:9**: Tỷ lệ widescreen
- **3:4**: Tỷ lệ portrait
- **9:16**: Tỷ lệ portrait dài (phù hợp cho story/reel)

### Cải tiến giao diện
- **Cuộn ngang**: Thanh chọn tỷ lệ khung hình có thể cuộn ngang để hiển thị tất cả các tùy chọn
- **LazyRow**: Sử dụng LazyRow thay vì Row để tối ưu hiệu suất và hỗ trợ cuộn
- **Spacing tối ưu**: Khoảng cách và padding được điều chỉnh để UI mượt mà hơn
- **Workflow đơn giản**: Loại bỏ nút phức tạp, chỉ cần chọn tỷ lệ và điều chỉnh tự do

### Tính năng nâng cao
- **Smart Aspect Ratio Application**: Tỷ lệ được áp dụng ngay lập tức khi chọn, sau đó cho phép điều chỉnh tự do
- **Independent Dimension Control**: Có thể điều chỉnh chiều dài và rộng độc lập sau khi áp dụng tỷ lệ
- **Intuitive Workflow**: Quy trình đơn giản: chọn tỷ lệ → áp dụng tự động → điều chỉnh tự do
- **No Constraints**: Không bị ràng buộc bởi tỷ lệ ban đầu khi điều chỉnh

### Files liên quan
- `PhotoPreviewScreen.kt`: Màn hình chính chứa tính năng crop
- `PhotoCropView.kt`: Component xử lý logic cắt ảnh
- `strings.xml`: Chứa các chuỗi text cho UI

## Tính năng Xử lý Back Gesture và Dialog Xác nhận

### Mô tả
Tính năng xử lý back gesture an toàn cho màn hình PhotoPreviewScreen, ngăn chặn crash app khi người dùng swipe back hoặc nhấn nút back. Thay vào đó, hiển thị dialog xác nhận để người dùng quyết định có muốn hủy bỏ các thay đổi hay không.

### Vấn đề đã giải quyết
- **Camera Session Crash**: Lỗi "Session has been closed; further changes are illegal" khi navigate back
- **Unsafe Navigation**: Người dùng có thể vô tình mất công sức chỉnh sửa ảnh
- **Poor UX**: Không có cảnh báo khi hủy bỏ thay đổi

### Cách hoạt động
1. **Back Gesture Interception**: Sử dụng `BackHandler` để intercept gesture back
2. **Dialog Confirmation**: Hiển thị dialog xác nhận thay vì navigate trực tiếp
3. **Safe Camera Cleanup**: Đảm bảo camera session được đóng đúng cách
4. **User Choice**: Cho phép người dùng chọn tiếp tục chỉnh sửa hoặc hủy bỏ

### Thành phần chính
- **BackHandler**: Component xử lý gesture back trong PhotoPreviewScreen
- **DiscardChangesDialog**: Dialog xác nhận hủy bỏ thay đổi
- **DisposableEffect**: Cleanup camera resources khi component bị dispose
- **Camera2Manager cleanup**: Đóng camera session an toàn

### Dialog Options
- **"Hủy bỏ"**: Xác nhận hủy bỏ tất cả thay đổi và quay về CameraScreen
- **"Tiếp tục chỉnh sửa"**: Đóng dialog và tiếp tục chỉnh sửa ảnh

### Cải tiến an toàn
- **Resource Management**: Tự động cleanup camera resources
- **Error Handling**: Xử lý lỗi trong quá trình cleanup mà không crash app
- **Lifecycle Awareness**: Tích hợp với Android lifecycle để cleanup đúng thời điểm

### Files được cập nhật
- `PhotoPreviewScreen.kt`: Thêm BackHandler và DiscardChangesDialog
- `CameraScreen.kt`: Thêm DisposableEffect cho camera cleanup
- `strings.xml`: Thêm các string resources cho dialog

### Cách tích hợp
Tính năng được tích hợp vào PhotoPreviewScreen thông qua:
- Biến trạng thái `showCropView` để điều khiển hiển thị
- Biến `currentBitmap` để lưu trữ ảnh hiện tại
- Logic xử lý khi người dùng chọn "Crop" trong bottom bar

### Lưu ý kỹ thuật
- Sử dụng Jetpack Compose Canvas để vẽ overlay và handles
- Xử lý gesture drag để tương tác với khung cắt
- Tính toán tỷ lệ và kích thước để đảm bảo crop chính xác
- Hỗ trợ maintain aspect ratio khi cần thiết