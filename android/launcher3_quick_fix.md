# Launcher3 垂直间距快速调整方案

## 🚀 最简单的解决方案

如果您只是想快速增加垂直间距，可以按照以下步骤操作：

### 1. 找到并修改 dimens.xml

**文件位置：** `packages/apps/Launcher3/res/values/dimens.xml`

**查找并修改以下参数：**

```xml
<!-- 将原来的值（通常是 4dp-8dp）改为更大的值 -->
<dimen name="dynamic_grid_cell_padding_y">16dp</dimen>
```

### 2. 如果上述修改无效，尝试以下参数

在同一个 `dimens.xml` 文件中查找并修改：

```xml
<!-- 工作区垂直间距 -->
<dimen name="workspace_cell_padding_y">18dp</dimen>

<!-- 应用抽屉垂直间距 -->
<dimen name="all_apps_cell_padding_y">20dp</dimen>

<!-- 图标垂直内边距 -->
<dimen name="app_icon_padding_vertical">8dp</dimen>
```

### 3. 横屏模式适配

**文件位置：** `packages/apps/Launcher3/res/values-land/dimens.xml`

```xml
<!-- 横屏模式下的垂直间距 -->
<dimen name="dynamic_grid_cell_padding_y">14dp</dimen>
<dimen name="workspace_cell_padding_y">16dp</dimen>
```

## 📱 不同屏幕尺寸的推荐值

| 屏幕类型 | dynamic_grid_cell_padding_y | workspace_cell_padding_y |
|----------|----------------------------|--------------------------|
| 小屏手机 | 12dp | 14dp |
| 标准手机 | 16dp | 18dp |
| 大屏手机 | 18dp | 20dp |
| 平板设备 | 20dp | 24dp |

## ⚡ 快速测试方法

1. **修改文件后编译：**
   ```bash
   make Launcher3 -j8
   ```

2. **安装测试：**
   ```bash
   adb install -r out/target/product/.../Launcher3.apk
   ```

3. **如果是系统应用，需要推送到系统分区：**
   ```bash
   adb remount
   adb push Launcher3.apk /system/priv-app/Launcher3/
   adb reboot
   ```

## 🔧 高级调整（可选）

如果基础修改不够，可以在 `DeviceProfile.java` 中添加：

```java
// 在 updateIconSize 方法中
cellPaddingY = Math.max(
    res.getDimensionPixelSize(R.dimen.dynamic_grid_cell_padding_y),
    (int) (iconSizePx * 0.2f)  // 图标大小的20%作为垂直间距
);
```

## ❗ 注意事项

1. **备份原文件**：修改前请备份原始的 `dimens.xml`
2. **渐进式调整**：建议从小幅增加开始，避免间距过大
3. **多设备测试**：在不同屏幕尺寸的设备上测试效果
4. **检查布局**：确保修改后所有图标仍能正常显示

## 🎯 效果验证

修改完成后，检查以下位置的间距是否符合预期：

- ✅ 主屏幕图标行间距
- ✅ 应用抽屉列表间距  
- ✅ 文件夹内图标间距
- ✅ 横屏模式显示
- ✅ 不同分辨率设备显示

## 🔄 恢复默认设置

如果需要恢复原始设置，将修改的数值改回原来的值：

```xml
<!-- 通常的默认值 -->
<dimen name="dynamic_grid_cell_padding_y">6dp</dimen>
<dimen name="workspace_cell_padding_y">8dp</dimen>
<dimen name="all_apps_cell_padding_y">12dp</dimen>
```