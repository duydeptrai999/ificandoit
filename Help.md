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
- **Tương thích với Filter**: Adjustment có thể áp dụng lên ảnh đã có filter mà không làm mất filter đã áp dụng

### Cách sử dụng
1. Trong PhotoPreviewScreen, chọn tùy chọn "Adjust" từ bottom bar
2. Màn hình chia đôi: ảnh xem trước ở trên (3/4), bảng điều khiển ở dưới (1/4)
3. Kéo các slider để điều chỉnh - thay đổi hiển thị ngay lập tức trên ảnh chính
4. Cuộn trong bảng điều chỉnh để truy cập tất cả các điều khiển
5. Nhấn "Apply" để áp dụng hoặc "Cancel" để hủy bỏ

## Tính năng Color Adjustment (Điều chỉnh Màu sắc Chi tiết)

### Mô tả
Tính năng điều chỉnh màu sắc chi tiết cho phép người dùng chỉnh sửa từng kênh màu cụ thể (đỏ, cam, vàng, lục, lam, tím, hồng) với 3 thông số HSL (Hue, Saturation, Luminance) cho mỗi màu.

### Thành phần chính
- **PhotoColorAdjustView**: Component chính để điều chỉnh màu sắc chi tiết
- **ColorAdjustmentValues**: Data class chứa giá trị HSL cho từng kênh màu
- **ColorAdjustmentUtils**: Utility class để áp dụng điều chỉnh màu sắc lên bitmap
- **ColorChannel**: Enum định nghĩa các kênh màu có thể điều chỉnh

### Thiết kế UI/UX
- **Color Selection Bar**: Thanh chọn màu ngang hiển thị 7 màu chính (đỏ, cam, vàng, lục, lam, tím, hồng)
- **HSL Sliders**: 3 slider cho mỗi màu được chọn (Hue: -180 đến +180, Saturation: -100 đến +100, Luminance: -100 đến +100)
- **Scrollable Panel**: Panel chiếm 1/4 màn hình, có thể cuộn để truy cập đầy đủ các điều khiển
- **Real-time Preview**: Xem trước thay đổi ngay lập tức trên ảnh chính
- **Responsive Design**: Tối ưu cho thiết bị di động với touch-friendly controls

### Tính năng
- **7 kênh màu**: Đỏ, Cam, Vàng, Lục, Lam, Tím, Hồng
- **3 thông số HSL**: Hue (sắc độ), Saturation (độ bão hòa), Luminance (độ sáng)
- **Selective Color Editing**: Chỉnh sửa từng màu cụ thể mà không ảnh hưởng đến màu khác
- **Advanced Color Processing**: Xử lý màu sắc dựa trên thuật toán HSL với độ chính xác cao
- **Performance Optimized**: Sử dụng preview bitmap nhỏ hơn để tăng hiệu năng
- **Apply/Cancel**: Áp dụng hoặc hủy bỏ các thay đổi
- **Reset Function**: Khôi phục về trạng thái ban đầu

### Cách sử dụng
1. Trong PhotoPreviewScreen, chọn tùy chọn "Color" từ bottom bar (thay thế cho "Cutout")
2. Màn hình chia đôi: ảnh xem trước ở trên (3/4), bảng điều chỉnh màu ở dưới (1/4)
3. Chọn màu muốn điều chỉnh từ thanh chọn màu ngang
4. Kéo các slider HSL để điều chỉnh màu đã chọn:
   - **Hue**: Thay đổi sắc độ màu (-180° đến +180°)
   - **Saturation**: Tăng/giảm độ bão hòa màu (-100% đến +100%)
   - **Luminance**: Tăng/giảm độ sáng của màu (-100% đến +100%)
5. Thay đổi hiển thị ngay lập tức trên ảnh chính
6. Cuộn trong panel để truy cập đầy đủ các điều khiển
7. Nhấn "Apply" để áp dụng hoặc "Cancel" để hủy bỏ

### Ví dụ sử dụng
- **Tăng độ sống động của bầu trời**: Chọn màu Lam, tăng Saturation
- **Làm ấm tông da**: Chọn màu Cam/Đỏ, điều chỉnh Hue và Luminance
- **Làm nổi bật cây lá**: Chọn màu Lục, tăng Saturation và Luminance
- **Tạo hiệu ứng sunset**: Chọn màu Vàng/Cam, điều chỉnh Hue về phía đỏ

## Tính năng Effect và Filter Selection

### Mô tả
Tính năng chọn Effect và Filter cho phép người dùng áp dụng các hiệu ứng và bộ lọc khác nhau lên ảnh.

### Cải tiến UI/UX (Phiên bản mới)
- **Panel vẫn hiển thị sau khi chọn**: Khi chọn effect hoặc filter, panel selection không tự động ẩn đi
- **Trải nghiệm liên tục**: Người dùng có thể thử nhiều effect/filter liên tiếp mà không cần mở lại panel
- **Tương tác tự nhiên**: Panel chỉ ẩn khi người dùng chủ động nhấn vào option khác hoặc nhấn lại cùng option

### Cách hoạt động
1. **Effect Selection**: Chọn "Effect" từ bottom bar → Panel hiển thị với các danh mục effect
2. **Chọn effect**: Nhấn vào effect bất kỳ → Effect được áp dụng ngay lập tức, panel vẫn hiển thị
3. **Thử nhiều effect**: Có thể chọn effect khác mà không cần mở lại panel
4. **Filter Selection**: Tương tự với effect, filter panel cũng vẫn hiển thị sau khi chọn
5. **Đóng panel**: Nhấn lại vào "Effect" hoặc "Filter" button, hoặc chọn option khác

### Lợi ích
- **Tiết kiệm thao tác**: Không cần mở lại panel mỗi lần muốn thử effect/filter khác
- **Trải nghiệm mượt mà**: Workflow chỉnh sửa ảnh trở nên liên tục và tự nhiên hơn
- **Tăng hiệu quả**: Người dùng có thể so sánh và thử nghiệm nhiều effect/filter nhanh chóng

### Workflow Filter + Adjustment
**Tính năng mới**: Có thể áp dụng adjustment lên ảnh đã có filter

**Cách hoạt động**:
1. **Áp dụng Filter trước**: Chọn filter từ danh sách (Vintage, Black & White, etc.)
2. **Sau đó Adjust**: Chọn "Adjust" để tinh chỉnh ảnh đã có filter
3. **Adjustment được áp dụng lên ảnh đã có filter**: Không bị mất filter đã chọn
4. **Kết quả**: Ảnh cuối cùng có cả filter và adjustment

**Lưu ý kỹ thuật**: PhotoAdjustView nhận `currentBitmap` (ảnh đã có filter) thay vì `rawPhotoBitmap` (ảnh gốc) để đảm bảo adjustment được áp dụng đúng cách

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