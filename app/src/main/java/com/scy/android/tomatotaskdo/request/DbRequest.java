package com.scy.android.tomatotaskdo.request;

import android.content.Context;

import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 沈程阳
 * created by scy on 2019/4/9 10:03
 * 邮箱：1797484636@qq.com
 */
public class DbRequest {

    public static User getCurrentUser(Context context) {
        long id = SpUtil.getCurrentUser(context, ConstValues.CURRENT_USER_ID);
        User user = findUserById(id);
        return user;
    }

    public static User findUserById(long id){
        User user = LitePal.find(User.class,id,true);
        return user;
    }

    public static List<User> findUsers(){
        List<User> users = LitePal.findAll(User.class,true);
        return users;
    }

    public static int getCurrentUserTodayFocusTime(Context context, User user) {
        List<FocusTime> focusTimes = user.getFocusTimes();
        for (FocusTime ft:focusTimes) {
            if (ft.getDate().equals(TimeUtil.formatFocusTime())) {
                return ft.getTime();
            }
        }
        return 0;
    }

    public static int getCurrentUserFocusTime(Context context, User user) {
        List<FocusTime> focusTimes = user.getFocusTimes();
        int time = 0;
        for (FocusTime ft:focusTimes) {
            time += ft.getTime();
        }
        return time;
    }

    public static void saveHeaderImg(String uri, Context context) {
        User user = getCurrentUser(context);
        user.setHeaderImageUri(uri);
        user.update(user.getId());
    }

    public static String getHeaderImg(Context context) {
        User user = getCurrentUser(context);
        return user.getHeaderImageUri();
    }

    public static List<Task> getCurrentUserTasks(Context context, User user) {
        List<Task> tasks = user.getTasks();
        return tasks;
    }

    public static List<Task> getCurrentUserTodayUnFinishTasks(Context context, User user) {
        List<Task> tasks = user.getTasks();
        List<Task> tasks1 = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getStartTime().equals(TimeUtil.formatTaskTime()) && task.getIsFinished().equals("未完成")) {
                tasks1.add(task);
            }
        }
        return tasks1;
    }

    public static Task getCurrentUserTodayUnFinishSelectTask(Context context, User user, String des) {
        List<Task> tasks = getCurrentUserTodayUnFinishTasks(context, user);
        for (Task task: tasks) {
            if (task.gettDescription().equals(des)) {
                return task;
            }
        }
        return null;
    }
}
