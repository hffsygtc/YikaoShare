package com.info.yikao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * by BiTiDaddy on 2023/5/11 09:45
 */
public class ParentNoInterceptRv extends RecyclerView {
    public ParentNoInterceptRv(@NonNull Context context) {
        super(context);
    }

    public ParentNoInterceptRv(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentNoInterceptRv(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
