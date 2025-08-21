# Codebase Structure Summary

## Camera Module

### UI/Screens
- **PhotoPreviewScreen** (Màn hình xem trước và chỉnh sửa ảnh với UI tối ưu)
  - displayPhoto, toggleEditMode, handleBackPress, savePhoto
  - Split-screen layout: photo preview (3/4) + adjustment controls (1/4)
  - Real-time preview updates với onPreviewUpdate callback
  - State management cho edit mode và photo adjustments
  - Tích hợp PhotoAdjustView với live preview system

- **PhotoAdjustView** (Component điều chỉnh ảnh tối ưu hóa - Updated)
  - **Enhanced Panel Height**: Tăng weight từ 1f lên 1.3f cho nhiều không gian hơn
  - **Optimized Padding**: Giảm padding từ 16dp xuống 12dp
  - Compact design: chỉ hiển thị adjustment controls, không có duplicate preview
  - Các slider điều chỉnh: brightness, contrast, saturation, highlights, shadows, warmth, tint được tối ưu
    - Icon size: 16dp (giảm từ 20dp)
    - Text styles: bodySmall cho labels, labelSmall cho values
    - Vertical padding: 4dp (giảm từ 8dp)
    - Icon-text spacing: 6dp (giảm từ 8dp)
  - Live preview callback system (onPreviewUpdate) cho real-time updates
  - Linear progress indicator thay vì circular
  - Scrollable interface với AdjustmentControlsPanel
  - Apply/cancel functionality với instant visual feedback

### Components
- **AdjustTopBar** (Top bar cho adjustment view)
  - Cancel, Reset, Apply buttons
  - Navigation và action handling

- **AdjustmentControlsPanel** (Panel chứa các điều khiển)
  - Scrollable column với basic và advanced adjustments
  - Organized slider groups cho better UX

- **AdjustmentSlider** (Custom slider component)
  - Value display và real-time updates
  - Consistent styling across all adjustments

- **PhotoColorAdjustView** (Advanced Color Adjustment Component - NEW)
  - Selective color editing cho 7 kênh màu: Red, Orange, Yellow, Green, Cyan, Blue, Purple, Magenta
  - ColorSelectionBar: thanh chọn màu ngang với visual indicators
  - ColorAdjustmentControlsPanel: 3 HSL sliders cho mỗi màu
  - Real-time preview với LaunchedEffect và coroutines
  - Performance optimized với preview bitmap scaling
  - Touch-friendly UI design cho mobile devices

### Utils
- **PhotoUtils** (Utility class cho photo processing)
  - applyAdjustments: áp dụng các điều chỉnh lên bitmap
  - Support cho tất cả adjustment types
  - Error handling và performance optimization

- **ColorAdjustmentUtils** (Advanced Color Processing Utility - NEW)
  - applyColorAdjustments: áp dụng selective color adjustments lên bitmap
  - HSL color space processing với high precision algorithms
  - Color range detection và mapping cho từng kênh màu
  - Performance optimized với coroutines và background processing
  - interpolateAdjustments: smooth transitions giữa adjustment states
  - hasAnyAdjustments: kiểm tra có adjustment nào được áp dụng

### Data Models
- **AdjustmentValues** (Data class cho adjustment parameters)
  - brightness, contrast, saturation, highlights, shadows, warmth, tint
  - Default values và validation

- **ColorAdjustmentValues** (Selective Color Adjustment Data Class - NEW)
  - HSL values cho 8 kênh màu: Red, Orange, Yellow, Green, Cyan, Blue, Purple, Magenta
  - Mỗi màu có 3 thông số: Hue (-180° to +180°), Saturation (-100% to +100%), Luminance (-100% to +100%)
  - hasAnyAdjustments(): kiểm tra có adjustment nào được áp dụng
  - Default values = 0f cho tất cả parameters

- **ColorChannel** (Enum cho Color Selection - NEW)
  - RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, MAGENTA
  - Mapping với ColorAdjustmentValues properties
  - UI color representation cho selection bar

## Architecture Features

### UI/UX Optimizations
- **Split-screen Design**: Tối ưu hóa không gian màn hình mobile
- **Real-time Preview**: Instant visual feedback khi điều chỉnh
- **Scrollable Interface**: Truy cập tất cả controls trong không gian hạn chế
- **Progress Indicators**: Visual feedback cho processing states

### State Management
- **Compose State**: Sử dụng remember và mutableStateOf
- **LaunchedEffect**: Xử lý side effects và async operations
- **Callback System**: Clean separation of concerns với callback functions

### Performance Considerations
- **Bitmap Processing**: Efficient handling của large images
- **Memory Management**: Proper cleanup và resource management
- **UI Responsiveness**: Non-blocking operations với coroutines