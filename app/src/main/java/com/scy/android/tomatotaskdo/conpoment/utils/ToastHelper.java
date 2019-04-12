package com.scy.android.tomatotaskdo.conpoment.utils;

import android.content.Context;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scy.android.tomatotaskdo.R;

/**
 * 沈程阳
 * created by scy on 2019/4/3 14:44
 * 邮箱：1797484636@qq.com
 */
public class ToastHelper {

    public static Toast mToast = null;
    /**
     * 弹出Toast
     * @param context 上下文对象
     * @param text    提示的文本
     * @param duration 持续时间（0：短；1：长）
     */
    public static void showToast(Context context, String text, int duration) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);
            TextView textView = view.findViewById(R.id.tv_issue);
            textView.setText(text);
            if (mToast == null) {
                mToast = new Toast(context);
            } else {
                textView.setText(text);
            }
            mToast.setDuration(duration);
            mToast.setView(view);
            mToast.show();
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(context, text, duration).show();
            Looper.loop();
        }
    }

    /**
     *
     * 弹出Toast
     * @param context 上下文对象
     * @param text    提示的文本
     * @param duration 持续时间（0：短；1：长）
     * @param gravity  位置（Gravity.CENTER;Gravity.TOP;...）
     */
    public static void showToast(Context context, String text, int duration,int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /**
     * 关闭Toast
     */
    public static void cancelToast(){
        if(mToast !=null){
            mToast.cancel();
        }
    }
}
