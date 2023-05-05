package com.info.yikao.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class FlowLayout extends ViewGroup {

    public interface ShowCountCallback {
        void showCount(int count);
    }

    private int showLines = -1;

    private int layoutCount = 0;

    private ShowCountCallback callback;

    public void setShowCountCallback(ShowCountCallback callback) {
        this.callback = callback;
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setShowLines(int showLines) {
        this.showLines = showLines;
    }

    public int getLayoutCount() {
        return layoutCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = 0, measuredHeight = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        Map<String, Integer> compute = compute(widthSize - getPaddingRight());

        //EXACTLY模式：对应于给定大小或者match_parent情况
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
            //AT_MOS模式：对应wrap-content（需要手动计算大小，否则相当于match_parent）
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute.get("allChildWidth");
        }

        if (heightMode == MeasureSpec.EXACTLY) {

            measuredHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            measuredHeight = compute.get("allChildHeight");
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lines = 0;
        int top = 0;
        layoutCount = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect rect = (Rect) getChildAt(i).getTag();

            if (showLines > 0) {
                //如果外部有设置的需要控制最大显示行数
                if (rect == null || rect.top != top) {
                    //不同的一行
                    lines++;
                    if (rect != null) {
                        top = rect.top;
                    }
                }

                if (lines > showLines) {
                    if (callback != null) {
                        callback.showCount(layoutCount);
                    }
                    break;
                }
            }
            layoutCount++;
            if (rect!= null){
                child.layout(rect.left, rect.top, rect.right, rect.bottom);
                final int pos = i;
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(pos);
                        }
                    }
                });
            }
        }
    }

    private Map<String, Integer> compute(int flowWidth) {
        //是否是单行
        boolean aRow = true;
        MarginLayoutParams marginParams;//子元素margin
        int rowsWidth = getPaddingLeft();//当前行已占宽度
        int columnHeight = getPaddingTop();//当前行顶部已占高度
        int rowsMaxHeight = 0;//当前行所有子元素的最大高度
        int lines = 1;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //获取元素测量宽度和高度
            int measureWidth = child.getMeasuredWidth();
            int measureHeight = child.getMeasuredHeight();
            //获取元素的margin
            marginParams = (MarginLayoutParams) child.getLayoutParams();
            //子元素所占宽度
            int childWidth = marginParams.leftMargin + marginParams.rightMargin + measureWidth;
            int childHeight = marginParams.topMargin + marginParams.bottomMargin + measureHeight;
            //判断是否换行：该行已占大小+该元素大小>父容器宽度 则换行
            rowsMaxHeight = Math.max(rowsMaxHeight, childHeight);
            //换行
            if (rowsWidth + childWidth > flowWidth) {
                lines++;

                if (showLines > 0) {
                    //如果外部有设置的需要控制最大显示行数
                    if (lines > showLines) {
                        child.setTag(null);
                        break;
                    }
                }

                //重置行宽度
                rowsWidth = getPaddingLeft();
                //累加上该行子元素最大高度
                columnHeight += rowsMaxHeight;
                //重置该行最大宽度
                rowsMaxHeight = childHeight;
                aRow = false;
            }


            //累加上该行子元素宽度
            rowsWidth += childWidth;
            child.setTag(new Rect(rowsWidth - childWidth + marginParams.leftMargin,
                    columnHeight + marginParams.topMargin,
                    rowsWidth - marginParams.rightMargin,
                    columnHeight + childHeight - marginParams.bottomMargin));


        }

        Map<String, Integer> flowMap = new HashMap<>();
        if (aRow) {
            flowMap.put("allChildWidth", rowsWidth);
        } else {
            flowMap.put("allChildWidth", flowWidth);
        }
        flowMap.put("allChildHeight", columnHeight + rowsMaxHeight + getPaddingBottom());

        return flowMap;
    }

    public interface onFlowItemClickListener {
        void onItemClick(int pos);
    }

    private onFlowItemClickListener listener;

    public void setItemClickListener(onFlowItemClickListener l) {
        this.listener = l;
    }


    public void changeChildColor(int pos, int color) {
        if (getChildCount() > pos) {
            View child = getChildAt(pos);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }

    }

    public void changeChildBg(int pos, int bgres) {
        if (getChildCount() > pos) {
            View child = getChildAt(pos);
            if (child instanceof TextView) {
                ((TextView) child).setBackgroundResource(bgres);
            }
        }

    }


}
