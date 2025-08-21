/**
 * Android 15 Launcher3 DeviceProfile.java 垂直间距调整示例
 * 文件位置：packages/apps/Launcher3/src/com/android/launcher3/DeviceProfile.java
 * 
 * 使用说明：
 * 1. 找到 DeviceProfile.java 文件中的相应方法
 * 2. 参考以下代码片段进行修改
 * 3. 主要修改 updateIconSize 和相关的间距计算方法
 */

public class DeviceProfile {
    
    // 垂直单元格间距
    public int cellPaddingY;
    
    // 图标大小相关
    public float iconSizePx;
    public int iconTextSizePx;
    
    /**
     * 更新图标大小和间距的方法 - 主要修改点
     */
    private void updateIconSize(float scale, Resources res) {
        // 原始代码可能类似这样：
        // cellPaddingY = res.getDimensionPixelSize(R.dimen.dynamic_grid_cell_padding_y);
        
        // 修改后的代码 - 增加垂直间距
        int basePaddingY = res.getDimensionPixelSize(R.dimen.dynamic_grid_cell_padding_y);
        
        // 方法1：直接增加固定值
        cellPaddingY = basePaddingY + res.getDimensionPixelSize(R.dimen.extra_vertical_padding);
        
        // 方法2：按比例增加
        cellPaddingY = (int) (basePaddingY * 1.5f);  // 增加50%的间距
        
        // 方法3：根据图标大小动态调整
        cellPaddingY = Math.max(basePaddingY, (int) (iconSizePx * 0.15f));
        
        // 方法4：设置最小和最大值限制
        int minPadding = res.getDimensionPixelSize(R.dimen.min_cell_padding_y);
        int maxPadding = res.getDimensionPixelSize(R.dimen.max_cell_padding_y);
        cellPaddingY = Math.min(Math.max(cellPaddingY, minPadding), maxPadding);
    }
    
    /**
     * 计算单元格高度的方法 - 可能需要调整
     */
    private int calculateCellHeight() {
        // 原始计算
        int cellHeight = iconSizePx + iconTextSizePx + cellPaddingY;
        
        // 如果需要额外的垂直空间，可以这样调整：
        int extraVerticalSpace = getResources().getDimensionPixelSize(R.dimen.extra_cell_height);
        cellHeight += extraVerticalSpace;
        
        return cellHeight;
    }
    
    /**
     * 获取工作区的垂直间距
     */
    public int getWorkspaceVerticalPadding() {
        // 返回工作区特定的垂直间距
        return getResources().getDimensionPixelSize(R.dimen.workspace_cell_padding_y);
    }
    
    /**
     * 获取应用抽屉的垂直间距
     */
    public int getAllAppsVerticalPadding() {
        // 返回应用抽屉特定的垂直间距
        return getResources().getDimensionPixelSize(R.dimen.all_apps_cell_padding_y);
    }
    
    /**
     * 动态调整间距的方法（高级用法）
     */
    public void adjustSpacingForDevice() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        
        // 根据屏幕密度调整
        float densityFactor = dm.density;
        if (densityFactor >= 3.0f) {  // xxhdpi 及以上
            cellPaddingY = (int) (cellPaddingY * 1.2f);
        } else if (densityFactor >= 2.0f) {  // xhdpi
            cellPaddingY = (int) (cellPaddingY * 1.1f);
        }
        
        // 根据屏幕高度调整
        int screenHeight = dm.heightPixels;
        if (screenHeight > 2000) {  // 高分辨率屏幕
            cellPaddingY += dpToPx(4);  // 增加 4dp
        }
    }
    
    /**
     * dp 转 px 的辅助方法
     */
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}

/**
 * 在 res/values/dimens.xml 中需要添加的额外配置：
 * 
 * <dimen name="extra_vertical_padding">4dp</dimen>
 * <dimen name="extra_cell_height">8dp</dimen>
 * <dimen name="min_cell_padding_y">6dp</dimen>
 * <dimen name="max_cell_padding_y">24dp</dimen>
 */