package com.scy.android.tomatotaskdo.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.longsh.optionframelibrary.OptionMaterialDialog;
import com.scy.android.tomatotaskdo.view.activity.MainActivity;
import com.scy.android.tomatotaskdo.view.activity.TomatoActivity;

/**
 * 沈程阳
 * created by scy on 2019/4/4 16:53
 * 邮箱：1797484636@qq.com
 */
public class TomatowhyDialog {
    public static void showDialog(Context context) {
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setTitle("小提示")
//                .setTitleTextColor(R.color.colorPrimary)
//                .setTitleTextSize((float) 22.5)
                .setMessage("这份报告是根据你和所有番茄任务用户对比做出的分析")
//                .setMessageTextColor(R.color.colorPrimary)
//                .setMessageTextSize((float) 16.5)
//                .setPositiveButtonTextColor(R.color.colorAccent)
//                .setNegativeButtonTextColor(R.color.colorPrimary)
//                .setPositiveButtonTextSize(15)
//                .setNegativeButtonTextSize(15)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("知道了",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                .show();
    }

}
