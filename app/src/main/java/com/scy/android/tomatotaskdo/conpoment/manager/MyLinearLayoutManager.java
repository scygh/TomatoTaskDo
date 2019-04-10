package com.scy.android.tomatotaskdo.conpoment.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 沈程阳
 * created by scy on 2019/4/10 11:41
 * 邮箱：1797484636@qq.com
 */
public class MyLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public MyLinearLayoutManager(Context context, boolean isScrollEnabled) {
        super(context);
        this.isScrollEnabled = isScrollEnabled;
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout, boolean isScrollEnabled) {
        super(context, orientation, reverseLayout);
        this.isScrollEnabled = isScrollEnabled;
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, boolean isScrollEnabled) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.isScrollEnabled = isScrollEnabled;
    }

    public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

}
