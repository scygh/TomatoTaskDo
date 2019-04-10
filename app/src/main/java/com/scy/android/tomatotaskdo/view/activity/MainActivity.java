package com.scy.android.tomatotaskdo.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.loadbutton.LoadButton;
import com.next.easynavigation.utils.NavigationUtil;
import com.next.easynavigation.view.EasyNavigationBar;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.service.TimerService;
import com.scy.android.tomatotaskdo.view.dialog.AddViewDialog;
import com.scy.android.tomatotaskdo.view.fragment.MineFragment;
import com.scy.android.tomatotaskdo.view.fragment.TaskFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigationBar)
    EasyNavigationBar mainNavigationBar;
    ImageView okIv;
    private String[] tabText = {"任务", "开始专注", "我的"};
    private int[] normalIcon = {R.drawable.task, R.drawable.times, R.drawable.mine};
    private int[] selectedIcon = {R.drawable.taskselected, R.drawable.timeselected, R.drawable.mineselected};
    private List<Fragment> mFragments = new ArrayList<>();
    public static LoadButton mLoadButton;
    private Button mBtnReset;
    private static TextView mainTime, todayTime;
    private int focusTime;
    private User currentUser;
    private  Handler mHandler = new Handler();
    private MyBroadcastReceiver mMyBroadcastReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mMyBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstValues.ACTION_TYPR_THREAD);
        mLocalBroadcastManager.registerReceiver(mMyBroadcastReceiver,intentFilter);
        //初始登录判断
        boolean isStartMain = SpUtil.getIsFirstVisit(this, ConstValues.START_MAIN);
        if (isStartMain) {
        } else {
            startActivity(GuideActivity.getIntent(this));
            finish();
        }

        //配置底部导航
        mFragments.add(new TaskFragment());
        mFragments.add(new MineFragment());
        mainNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectedIcon)
                .fragmentList(mFragments)
                .mode(EasyNavigationBar.MODE_ADD)
                .fragmentManager(getSupportFragmentManager())
                .normalTextColor(Color.parseColor("#8D8D8D"))   //Tab未选中时字体颜色
                .selectTextColor(Color.parseColor("#F1604D"))   //Tab选中时字体颜色
                .navigationBackground(Color.parseColor("#FFFFFF"))   //导航栏背景色
                .addAsFragment(false)
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int i) {
                        if (i == 1) {
                            showView();
                            if (SpUtil.getIsTiming(MainActivity.this, ConstValues.ISTIMING)) {
                               mLoadButton.isTiming();
                            }
                        } else if (i == 2) {
                            if (!Apis.checkLogin(MainActivity.this)) {
                                ToastHelper.showToast(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
                                startActivity(LoginActivity.getIntent(MainActivity.this));
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .build();
        mainNavigationBar.setAddViewLayout(createTaskView());
    }

    private View createTaskView() {
        ViewGroup view = (ViewGroup) View.inflate(MainActivity.this, R.layout.layout_add_view, null);
        okIv = view.findViewById(R.id.ok_iv);
        mLoadButton = view.findViewById(R.id.btn_status);
        mBtnReset = view.findViewById(R.id.btn_reset);
        mainTime = view.findViewById(R.id.add_view_maintime);
        todayTime = view.findViewById(R.id.add_view_todaytime);

        mLoadButton.setListenner(new LoadButton.LoadListenner() {
            @Override
            public void onClick(boolean isSuccessed) {
                if (isSuccessed) {
                    //成功状态点击事件
                    AddViewDialog.showDialog(MainActivity.this);
                }
            }

            @Override
            public void needLoading() {
            }

            @Override
            public void starttime(boolean start) {
                Intent intent = new Intent(MainActivity.this, TimerService.class);
                if (start) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                    SpUtil.setIsTiming(MainActivity.this ,ConstValues.ISTIMING, true);

                } else {
                    stopService(intent);
                    ToastHelper.showToast(MainActivity.this, "你开小差了，请重新开始", Toast.LENGTH_SHORT);
                    SpUtil.setIsTiming(MainActivity.this ,ConstValues.ISTIMING, false);
                    updateUI();
                }
            }
        });
        okIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation();
            }
        });
        return view;
    }

    public static void updateUI() {
        mLoadButton.reset();
        mainTime.setText("25:00");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                mLoadButton.reset();
                break;

            default:
                break;
        }
    }

    private void showView() {
        startAnimation();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                okIv.animate().rotation(360).setDuration(500);
            }
        });
        initAddViewData();
    }

    private void startAnimation() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int x = NavigationUtil.getScreenWidth(MainActivity.this) / 2;
                        int y = (int) (NavigationUtil.getScreenHeith(MainActivity.this) - NavigationUtil.dip2px(MainActivity.this, 25));
                        Animator animator = ViewAnimationUtils.createCircularReveal(mainNavigationBar.getAddViewLayout(), x, y, 0, mainNavigationBar.getAddViewLayout().getHeight());
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                mainNavigationBar.getAddViewLayout().setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.setDuration(300);
                        animator.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public  void closeAnimation() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                okIv.animate().rotation(0).setDuration(400);
            }
        });

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                int x = NavigationUtil.getScreenWidth(this) / 2;
                int y = (NavigationUtil.getScreenHeith(this) - NavigationUtil.dip2px(this, 25));
                Animator animator = ViewAnimationUtils.createCircularReveal(mainNavigationBar.getAddViewLayout(), x,
                        y, mainNavigationBar.getAddViewLayout().getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mainNavigationBar.getAddViewLayout().setVisibility(View.GONE);
                        //dismiss();
                    }
                });
                animator.setDuration(300);
                animator.start();
            }
        } catch (Exception e) {
        }
    }


    public void initAddViewData () {
        if (Apis.checkLogin(this)) {
            currentUser = DbRequest.getCurrentUser(this);
            focusTime = DbRequest.getCurrentUserTodayFocusTime(this, currentUser);
            todayTime.setText(""+ focusTime);
        } else {
            focusTime = SpUtil.getCurrentFocusTime(MainActivity.this, ConstValues.FOCUS_TIME);
            Log.d(TAG, "initAddViewData: "+ focusTime);
            todayTime.setText(""+ focusTime);
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mainTime.setText(intent.getStringExtra("mainTimeText"));
            todayTime.setText(intent.getStringExtra("todayTimeText"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mMyBroadcastReceiver);
    }

    private long firstTime=0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK://点击返回键
                long secondTime = System.currentTimeMillis();//以毫秒为单位
                if(secondTime -firstTime>2000){
                    Toast.makeText(this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                }else{
                    finish();
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
