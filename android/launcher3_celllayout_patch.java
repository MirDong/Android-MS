/**
 * Android 15 Launcher3 CellLayout.java 垂直间距调整示例
 * 文件位置：packages/apps/Launcher3/src/com/android/launcher3/CellLayout.java
 * 
 * 使用说明：
 * 1. 找到 CellLayout.java 文件中的相应方法
 * 2. 参考以下代码片段进行修改
 * 3. 主要修改布局参数设置和间距计算方法
 */

public class CellLayout extends ViewGroup {
    
    // 单元格间距
    private int mCellPaddingY;
    
    /**
     * 设置布局参数 - 主要修改点
     */
    private void setupLp(CellLayout.LayoutParams lp) {
        final int cellHSpan = lp.cellHSpan;
        final int cellVSpan = lp.cellVSpan;
        final int cellX = lp.cellX;
        final int cellY = lp.cellY;
        
        // 原始的宽高计算
        lp.width = (cellHSpan * mCellWidth) + ((cellHSpan - 1) * mWidthGap);
        lp.height = (cellVSpan * mCellHeight) + ((cellVSpan - 1) * mHeightGap);
        
        // 原始的位置计算
        lp.x = (cellX * (mCellWidth + mWidthGap)) + mPaddingLeft;
        lp.y = (cellY * (mCellHeight + mHeightGap)) + mPaddingTop;
        
        // ===== 垂直间距调整部分 =====
        
        // 方法1：直接增加垂直边距
        int extraVerticalMargin = mCellPaddingY / 2;
        lp.topMargin = extraVerticalMargin;
        lp.bottomMargin = extraVerticalMargin;
        
        // 方法2：调整 Y 坐标以增加行间距
        int additionalRowSpacing = getResources().getDimensionPixelSize(R.dimen.additional_row_spacing);
        lp.y += (cellY * additionalRowSpacing);
        
        // 方法3：动态调整高度间隙
        if (cellVSpan > 1) {
            // 对于跨多行的项目，增加额外的垂直空间
            int extraHeightGap = getResources().getDimensionPixelSize(R.dimen.extra_height_gap);
            lp.height += ((cellVSpan - 1) * extraHeightGap);
        }
    }
    
    /**
     * 测量子视图 - 可能需要调整
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        
        // 计算可用空间时考虑额外的垂直间距
        int extraVerticalSpace = getExtraVerticalSpacing();
        
        if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            heightSpecSize = getPaddingTop() + getPaddingBottom() + 
                           (mCountY * mCellHeight) + ((mCountY - 1) * mHeightGap) +
                           extraVerticalSpace;
            heightSpecMode = MeasureSpec.EXACTLY;
        }
        
        // 重新计算高度间隙以包含额外的垂直间距
        mHeightGap = Math.max(0, (heightSpecSize - getPaddingTop() - getPaddingBottom() - 
                               (mCountY * mCellHeight) - extraVerticalSpace) / Math.max(1, (mCountY - 1)));
        
        setMeasuredDimension(widthSpecSize, heightSpecSize);
        
        // 测量所有子视图
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child);
            }
        }
    }
    
    /**
     * 获取额外的垂直间距
     */
    private int getExtraVerticalSpacing() {
        // 根据行数计算额外的垂直间距
        int baseSpacing = getResources().getDimensionPixelSize(R.dimen.base_extra_vertical_spacing);
        return baseSpacing * (mCountY - 1);  // 每行之间添加额外间距
    }
    
    /**
     * 布局子视图 - 应用垂直间距调整
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                CellLayout.LayoutParams lp = (CellLayout.LayoutParams) child.getLayoutParams();
                
                // 应用额外的垂直偏移
                int verticalOffset = calculateVerticalOffset(lp.cellY);
                
                int childLeft = lp.x;
                int childTop = lp.y + verticalOffset;
                child.layout(childLeft, childTop, 
                           childLeft + lp.width, childTop + lp.height);
            }
        }
    }
    
    /**
     * 计算基于行位置的垂直偏移
     */
    private int calculateVerticalOffset(int row) {
        // 为每一行添加累积的额外间距
        int extraSpacingPerRow = getResources().getDimensionPixelSize(R.dimen.extra_spacing_per_row);
        return row * extraSpacingPerRow;
    }
    
    /**
     * 更新单元格尺寸 - 包含垂直间距调整
     */
    public void setCellDimensions(int cellWidth, int cellHeight) {
        mCellWidth = cellWidth;
        
        // 调整单元格高度以包含额外的垂直间距
        int extraCellHeight = getResources().getDimensionPixelSize(R.dimen.extra_cell_height);
        mCellHeight = cellHeight + extraCellHeight;
        
        requestLayout();
    }
    
    /**
     * 设置垂直间距
     */
    public void setVerticalPadding(int padding) {
        mCellPaddingY = padding;
        requestLayout();
    }
    
    /**
     * 获取推荐的最小行间距
     */
    public int getRecommendedMinRowSpacing() {
        return getResources().getDimensionPixelSize(R.dimen.min_row_spacing);
    }
}

/**
 * 在 res/values/dimens.xml 中需要添加的配置：
 * 
 * <!-- 额外的行间距 -->
 * <dimen name="additional_row_spacing">4dp</dimen>
 * 
 * <!-- 额外的高度间隙 -->
 * <dimen name="extra_height_gap">2dp</dimen>
 * 
 * <!-- 基础额外垂直间距 -->
 * <dimen name="base_extra_vertical_spacing">2dp</dimen>
 * 
 * <!-- 每行的额外间距 -->
 * <dimen name="extra_spacing_per_row">3dp</dimen>
 * 
 * <!-- 额外的单元格高度 -->
 * <dimen name="extra_cell_height">6dp</dimen>
 * 
 * <!-- 最小行间距 -->
 * <dimen name="min_row_spacing">8dp</dimen>
 */