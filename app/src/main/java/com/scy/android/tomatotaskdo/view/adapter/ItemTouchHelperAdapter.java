package com.scy.android.tomatotaskdo.view.adapter;

/**
 * 沈程阳
 * created by scy on 2019/4/10 13:00
 * 邮箱：1797484636@qq.com
 */
public interface ItemTouchHelperAdapter {
        //数据交换
        void onItemMove(int fromPosition,int toPosition);
        //数据删除
        void onItemDissmiss(int position);
}
