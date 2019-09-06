package com.gmcp.gm.common.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 一种流式布局的LayoutManager
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = FlowLayoutManager.class.getSimpleName();
    private final FlowLayoutManager self = this;

    protected int width, height;
    private int left;
    private int top;
    //最大容器的宽度
    private int usedMaxWidth;
    //竖直方向上的偏移量
    private int verticalScrollOffset = 0;

    int getTotalHeight() {
        return totalHeight;
    }

    //计算显示的内容的高度
    private int totalHeight = 0;
    private Row row = new Row();

    public int getLineRows() {
        return lineRows.size();
    }

    private List<Row> lineRows = new ArrayList<>();

    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    public FlowLayoutManager() {
        //设置主动测量规则,适应recyclerView高度为wrap_content
        setAutoMeasureEnabled(true);
    }

    //每个item的定义
    public class Item {
        int useHeight;
        View view;

        void setRect(Rect rect) {
            this.rect = rect;
        }

        Rect rect;

        Item(int useHeight, View view, Rect rect) {
            this.useHeight = useHeight;
            this.view = view;
            this.rect = rect;
        }
    }

    //行信息的定义
    public class Row {
        void setCuTop(float cuTop) {
            this.cuTop = cuTop;
        }

        void setMaxHeight(float maxHeight) {
            this.maxHeight = maxHeight;
        }
        //每一行的头部坐标
        float cuTop;
        //每一行需要占据的最大高度
        float maxHeight;
        //每一行存储的item
        List<Item> views = new ArrayList<>();

        void addViews(Item view) {
            views.add(view);
        }
    }

    public void setChildLayoutListener(ChildLayoutListener childLayoutListener) {
        this.childLayoutListener = childLayoutListener;
    }

    private ChildLayoutListener childLayoutListener;

    public interface ChildLayoutListener {
        void onLayout(int maxChild, int lineCount);

        void end(int lineCount);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private boolean hasMaxItem;
    private boolean hasTotalLine;

    //该方法主要用来获取每一个item在屏幕上占据的位置
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            verticalScrollOffset = 0;
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler);
        if (getChildCount() == 0) {
            width = getWidth();
            height = getHeight();
            left = getPaddingLeft();
            int right = getPaddingRight();
            top = getPaddingTop();
            usedMaxWidth = width - left - right;
        }
        totalHeight = 0;
        int cuLineTop = top;
        //当前行使用的宽度
        int cuLineWidth = 0;
        int itemLeft;
        int itemTop;
        int maxHeightItem = 0;
        int maxLines = 0;
        row = new Row();
        lineRows.clear();
        allItemFrames.clear();
        removeAllViews();
        for (int i = 0; i < getItemCount(); i++) {
            View childAt = recycler.getViewForPosition(i);
            if (View.GONE == childAt.getVisibility()) {
                continue;
            }
            measureChildWithMargins(childAt, 0, 0);
            int childWidth = getDecoratedMeasuredWidth(childAt);
            int childHeight = getDecoratedMeasuredHeight(childAt);
            //如果加上当前的item还小于最大的宽度的话
            if (cuLineWidth + childWidth <= usedMaxWidth) {
                itemLeft = left + cuLineWidth;
                itemTop = cuLineTop;
                Rect frame = allItemFrames.get(i);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                allItemFrames.put(i, frame);
                cuLineWidth += childWidth;
                maxHeightItem = Math.max(maxHeightItem, childHeight);
                row.addViews(new Item(childHeight, childAt, frame));
                row.setCuTop(cuLineTop);
                row.setMaxHeight(maxHeightItem);
                if (totalHeight + childHeight > getVerticalSpace() && !hasMaxItem) {
                    if (childLayoutListener != null) {
                        childLayoutListener.onLayout(i, lineRows.size());
                    }
                    hasMaxItem = true;
                }
            } else {
                //换行
                formatAboveRow();
                //判断换行的时候item是否大于了总高度
                cuLineTop += maxHeightItem;
                totalHeight += maxHeightItem;

                itemTop = cuLineTop;
                itemLeft = left;
                Rect frame = allItemFrames.get(i);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                allItemFrames.put(i, frame);
                cuLineWidth = childWidth;
                maxHeightItem = childHeight;
                if (totalHeight + childHeight > getVerticalSpace() && !hasMaxItem) {
                    if (childLayoutListener != null) {
                        childLayoutListener.onLayout(i, lineRows.size());
                    }
                    hasMaxItem = true;
                }
                row.addViews(new Item(childHeight, childAt, frame));
                row.setCuTop(cuLineTop);
                row.setMaxHeight(maxHeightItem);
            }
            //不要忘了最后一行进行刷新下布局
            if (i == getItemCount() - 1) {
                formatAboveRow();
                totalHeight += maxHeightItem;
            }
        }
        totalHeight = Math.max(totalHeight, getVerticalSpace());
        if (childLayoutListener != null && !hasTotalLine) {
            childLayoutListener.end(lineRows.size());
            hasTotalLine = true;
        }
        fillLayout(recycler, state);

    }

    //对出现在屏幕上的item进行展示，超出屏幕的item回收到缓存中
    private void fillLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout() || getItemCount() == 0) { // 跳过preLayout，preLayout主要用于支持动画
            return;
        }
        // 当前scroll offset状态下的显示区域
        Rect displayFrame = new Rect(getPaddingLeft(), getPaddingTop() + verticalScrollOffset,
                getWidth() - getPaddingRight(), verticalScrollOffset + (getHeight() - getPaddingBottom()));
        //对所有的行信息进行遍历
        for (int j = 0; j < lineRows.size(); j++) {
            Row row = lineRows.get(j);
            float lineTop = row.cuTop;
            float lineBottom = lineTop + row.maxHeight;
            //如果该行在屏幕中，进行放置item
            List<Item> views = row.views;
            for (int i = 0; i < views.size(); i++) {
                View scrap = views.get(i).view;
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);
                Rect frame = views.get(i).rect;
                //将这个item布局出来
                layoutDecoratedWithMargins(scrap,
                        frame.left,
                        frame.top - verticalScrollOffset,
                        frame.right,
                        frame.bottom - verticalScrollOffset);
            }
        }
    }

    /**
     * 计算每一行没有居中的viewer，让居中显示
     */
    private void formatAboveRow() {
        List<Item> views = row.views;
        for (int i = 0; i < views.size(); i++) {
            Item item = views.get(i);
            View view = item.view;
            int position = getPosition(view);
            //如果该item的位置不在该行中间位置的话，进行重新放置
            if (allItemFrames.get(position).top < row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2) {
                Rect frame = allItemFrames.get(position);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(allItemFrames.get(position).left, (int) (row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2),
                        allItemFrames.get(position).right, (int) (row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2 + getDecoratedMeasuredHeight(view)));
                allItemFrames.put(position, frame);
                item.setRect(frame);
                views.set(i, item);
            }
        }
        row.views = views;
        lineRows.add(row);
        row = new Row();
    }

    /**
     * 竖直方向需要滑动的条件
     */
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    //监听竖直方向滑动的偏移量
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        //实际要滑动的距离
        int travel = dy;

        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {//限制滑动到顶部之后，不让继续向上滑动了
            travel = -verticalScrollOffset;//verticalScrollOffset=0
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;//verticalScrollOffset=totalHeight - getVerticalSpace()
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;

        // 平移容器内的item
        offsetChildrenVertical(-travel);
        fillLayout(recycler, state);
        return travel;
    }

    private int getVerticalSpace() {
        return self.getHeight() - self.getPaddingBottom() - self.getPaddingTop();
    }

    public int getHorizontalSpace() {
        return self.getWidth() - self.getPaddingLeft() - self.getPaddingRight();
    }
}
