package com.scy.android.tomatotaskdo.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.activity.MainActivity;

import org.litepal.LitePal;

import java.util.List;


/**
 * 沈程阳
 * created by scy on 2019/4/4 16:08
 * 邮箱：1797484636@qq.com
 */
public class TimerService extends Service {
    private static final String TAG = "TimerService";

    CountDownTimer timer;
    private String mainTimeText;
    private int time,todayTimeText;
    private int focusTime;
    private LocalBroadcastManager mLocalBroadcastManager;


    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCountDownTimer();
        return super.onStartCommand(intent, START_STICKY_COMPATIBILITY, startId);
    }

    private void startCountDownTimer() {
        timer = new CountDownTimer(25*1000*60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mainTimeText = TimeUtil.formatTime(millisUntilFinished);
                time = Integer.parseInt(TimeUtil.formatMinuteTime(25*1000*60-millisUntilFinished));
                focusTime = SpUtil.getCurrentFocusTime(TimerService.this ,ConstValues.FOCUS_TIME);
                todayTimeText = time + focusTime;
                Intent intent = new Intent(ConstValues.ACTION_TYPR_THREAD);
                intent.putExtra("mainTimeText","" + mainTimeText);
                intent.putExtra("todayTimeText","" + todayTimeText);
                mLocalBroadcastManager.sendBroadcast(intent);
                Log.d(TAG, "onTick: ");
            }

            @Override
            public void onFinish() {
                MainActivity.mLoadButton.loadSuccessed();
                saveAddViewData();
                Log.d(TAG, "onFinish: ");
                stopSelf();
            }

        }.start();
    }

    public void saveAddViewData() {
        focusTime = focusTime + time;
        if (checkLogin()) {
            User user = DbRequest.getCurrentUser(this);
            List<FocusTime> focusTimes = user.getFocusTimes();
            for (FocusTime ft:focusTimes) {
                if (ft.getDate().equals(TimeUtil.formatFocusTime())) {
                    ft.setTime(focusTime);
                    ft.update(ft.getId());
                    return;
                }
            }
            FocusTime f = new FocusTime();
            f.setDate(TimeUtil.formatFocusTime());
            f.setTime(focusTime);
            f.setUser(user);
            f.save();

        } else {
            SpUtil.setCurrentFocusTime(this, ConstValues.FOCUS_TIME, focusTime);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void setForegroundService() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        String channelName = getString(R.string.channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("1", channelName, importance);
        channel.setDescription("专注时间倒计时服务");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1");
        builder.setSmallIcon(R.drawable.logo2)
                .setContentTitle("Tomato")
                .setContentText("Tomato正在记录你的学习时间")
                .setContentIntent(pendingIntent)
                .setOngoing(true);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.onFinish();
        timer.cancel();
    }

    public boolean checkLogin() {
        Boolean isLogin = SpUtil.getIsLogin(this, ConstValues.LOGIN);
        return isLogin;
    }
}
