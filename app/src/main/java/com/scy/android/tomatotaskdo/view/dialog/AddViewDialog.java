package com.scy.android.tomatotaskdo.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.longsh.optionframelibrary.OptionMaterialDialog;
import com.scy.android.tomatotaskdo.view.activity.MainActivity;

/**
 * 沈程阳
 * created by scy on 2019/4/4 16:53
 * 邮箱：1797484636@qq.com
 */
public class AddViewDialog{
    public static void showDialog(Context context) {
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setTitle("太棒了")
//                .setTitleTextColor(R.color.colorPrimary)
//                .setTitleTextSize((float) 22.5)
                .setMessage("你完成了一次专注！休息一会再来吧")
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
                .setNegativeButton("不了我还能继续学习",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                MainActivity.updateUI();
                            }
                        })
                .show();
    }

}
