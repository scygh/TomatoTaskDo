package com.scy.android.tomatotaskdo.conpoment.utils;

import android.content.Context;

import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.activity.RankActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 沈程阳
 * created by scy on 2019/4/11 22:08
 * 邮箱：1797484636@qq.com
 */
public class SortUtil {

    public static List<User> sort(List<User> users, final Context context) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int a = DbRequest.getCurrentUserTodayFocusTime(context, o1);
                int b = DbRequest.getCurrentUserTodayFocusTime(context, o2);
                return  b-a;
            }
        });
        return users;
    }

    public static List<User> sortSumTime(List<User> users, final Context context) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int a = DbRequest.getCurrentUserFocusTime(context, o1);
                int b = DbRequest.getCurrentUserFocusTime(context, o2);
                return  b-a;
            }
        });
        return users;
    }

}
