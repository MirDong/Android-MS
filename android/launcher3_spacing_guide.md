# Android 15 Launcher3 垂直间距调整指南

## 问题描述
Android 15 Launcher3 中应用图标的上下两行间距过小，需要调整垂直间距以提升用户体验。

## 解决方案

### 1. 关键文件位置

在 Launcher3 源码中，控制图标间距的主要文件包括：

```
packages/apps/Launcher3/
├── res/
│   ├── values/
│   │   ├── dimens.xml              # 尺寸定义
│   │   └── config.xml              # 配置参数
│   ├── values-land/
│   │   └── dimens.xml              # 横屏尺寸
│   └── layout/
│       ├── all_apps.xml            # 应用列表布局
│       └── workspace_screen.xml    # 工作区布局
├── src/com/android/launcher3/
│   ├── CellLayout.java             # 网格布局控制
│   ├── DeviceProfile.java          # 设备配置文件
│   └── Workspace.java              # 工作区管理
```

### 2. 主要调整参数

#### 2.1 在 `res/values/dimens.xml` 中调整基础间距：

```xml
<!-- 应用图标垂直间距 -->
<dimen name="dynamic_grid_cell_padding_y">8dp</dimen>

<!-- 工作区图标间距 -->
<dimen name="workspace_cell_padding_y">12dp</dimen>

<!-- 应用抽屉中的垂直间距 -->
<dimen name="all_apps_cell_padding_y">16dp</dimen>

<!-- 图标行间距 -->
<dimen name="app_icon_padding_vertical">4dp</dimen>

<!-- 网格间距 -->
<dimen name="dynamic_grid_cell_padding_top">6dp</dimen>
<dimen name="dynamic_grid_cell_padding_bottom">6dp</dimen>
```

#### 2.2 在 `DeviceProfile.java` 中的关键参数：

```java
// 垂直单元格间距
public int cellPaddingY;

// 图标大小和间距比例
public float iconSizePx;
public int iconTextSizePx;

// 计算垂直间距的方法
private void updateIconSize(float scale, Resources res) {
    // 增加垂直间距
    cellPaddingY = (int) (res.getDimensionPixelSize(R.dimen.dynamic_grid_cell_padding_y) * scale);
}
```

### 3. 具体修改步骤

#### 步骤 1：修改基础尺寸文件

编辑 `res/values/dimens.xml`：

```xml
<!-- 原值可能是 4dp 或 6dp，增加到 12dp -->
<dimen name="dynamic_grid_cell_padding_y">12dp</dimen>

<!-- 工作区垂直间距，从 8dp 增加到 16dp -->
<dimen name="workspace_cell_padding_y">16dp</dimen>

<!-- 应用抽屉垂直间距 -->
<dimen name="all_apps_cell_padding_y">20dp</dimen>
```

#### 步骤 2：修改横屏配置

编辑 `res/values-land/dimens.xml`：

```xml
<!-- 横屏模式下的垂直间距 -->
<dimen name="dynamic_grid_cell_padding_y">10dp</dimen>
<dimen name="workspace_cell_padding_y">14dp</dimen>
```

#### 步骤 3：修改 DeviceProfile.java

在 `DeviceProfile.java` 中找到计算间距的方法，调整垂直间距计算：

```java
// 在 updateIconSize 方法中
cellPaddingY = Math.max(
    res.getDimensionPixelSize(R.dimen.dynamic_grid_cell_padding_y),
    (int) (iconSizePx * 0.15f)  // 增加比例从 0.1f 到 0.15f
);
```

#### 步骤 4：修改 CellLayout.java

在 `CellLayout.java` 中调整网格计算：

```java
// 在 setupLp 方法中增加垂直边距
private void setupLp(CellLayout.LayoutParams lp) {
    // ... 现有代码 ...
    
    // 增加垂直间距
    int verticalMargin = mCellPaddingY / 2;
    lp.topMargin += verticalMargin;
    lp.bottomMargin += verticalMargin;
}
```

### 4. 高级配置选项

#### 4.1 动态间距配置

在 `res/values/config.xml` 中添加动态配置：

```xml
<!-- 启用动态间距调整 -->
<bool name="config_dynamic_spacing_enabled">true</bool>

<!-- 间距调整因子 -->
<fraction name="config_vertical_spacing_factor">150%</fraction>
```

#### 4.2 不同密度屏幕的适配

创建不同密度的资源文件：

```
res/values-hdpi/dimens.xml     # 高密度屏幕
res/values-xhdpi/dimens.xml    # 超高密度屏幕
res/values-xxhdpi/dimens.xml   # 超超高密度屏幕
```

每个文件中设置适合该密度的间距值。

### 5. 编译和测试

#### 5.1 编译命令

```bash
# 在 Android 源码根目录执行
source build/envsetup.sh
lunch aosp_arm64-eng
make Launcher3 -j8
```

#### 5.2 安装测试

```bash
# 安装到设备
adb install -r out/target/product/generic_arm64/system/priv-app/Launcher3/Launcher3.apk

# 或者推送到系统分区
adb push out/target/product/generic_arm64/system/priv-app/Launcher3/Launcher3.apk /system/priv-app/Launcher3/
adb reboot
```

### 6. 验证效果

修改完成后，检查以下方面：

1. **主屏幕**：图标行之间的垂直间距是否增加
2. **应用抽屉**：应用图标列表的行间距是否合适
3. **横屏模式**：横屏时的间距是否协调
4. **不同屏幕尺寸**：在不同设备上的显示效果

### 7. 常见问题和解决方案

#### 问题 1：间距过大导致显示不完整

**解决方案**：适当减小间距值，或者调整图标大小：

```xml
<dimen name="dynamic_grid_cell_padding_y">10dp</dimen>  <!-- 减小间距 -->
<dimen name="app_icon_size">48dp</dimen>                <!-- 减小图标 -->
```

#### 问题 2：不同屏幕密度显示不一致

**解决方案**：为每种密度创建专门的配置文件。

#### 问题 3：修改后无效果

**解决方案**：
1. 清理缓存：`make clean`
2. 确保修改了正确的资源文件
3. 检查是否有其他覆盖配置

### 8. 推荐的间距值

根据不同使用场景，推荐以下间距值：

| 场景 | 推荐值 | 说明 |
|------|--------|------|
| 紧凑布局 | 8dp | 最大化屏幕利用率 |
| 标准布局 | 12dp | 平衡美观和实用性 |
| 宽松布局 | 16dp | 更好的视觉体验 |
| 大屏设备 | 20dp | 适合平板等大屏设备 |

### 9. 总结

通过修改上述配置文件和代码，可以有效调整 Launcher3 的垂直间距。建议：

1. 先从 `dimens.xml` 开始调整基础值
2. 根据实际效果微调其他参数
3. 在不同设备上测试效果
4. 考虑用户习惯和设备特性

记住要备份原始文件，以便需要时恢复默认设置。