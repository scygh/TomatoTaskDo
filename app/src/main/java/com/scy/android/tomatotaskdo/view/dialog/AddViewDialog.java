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
public class AddViewDialog {
    public static void showDialog(Context context) {
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setTitle("提示")
                .setMessage("你完成了一次短暂的专注！休息一会再来吧")
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
                                //焦点消失，更新主界面
                                MainActivity.updateUI();
                            }
                        })
                .show();
    }

}
