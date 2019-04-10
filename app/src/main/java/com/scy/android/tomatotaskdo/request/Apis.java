package com.scy.android.tomatotaskdo.request;

import android.content.Context;

import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;

/**
 * 沈程阳
 * created by scy on 2019/4/10 15:42
 * 邮箱：1797484636@qq.com
 */
public class Apis {

    public static boolean checkLogin(Context context) {
        Boolean isLogin = SpUtil.getIsLogin(context, ConstValues.LOGIN);
        return isLogin;
    }
}
