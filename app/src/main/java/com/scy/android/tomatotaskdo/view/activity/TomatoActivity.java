package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.SortUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.customview.LineView;
import com.scy.android.tomatotaskdo.view.dialog.TomatowhyDialog;
import com.scy.android.tomatotaskdo.view.fragment.DatePickerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TomatoActivity extends BaseActivity {

    private static final String TAG = "TomatoActivity";
    @BindView(R.id.tomato_date)
    ImageView tomatoDate;
    @BindView(R.id.tomato_iv_why)
    ImageView tomatoIvWhy;
    @BindView(R.id.tomato_sum_time)
    TextView tomatoSumTime;
    @BindView(R.id.tomato_sum_rank)
    TextView tomatoSumRank;
    @BindView(R.id.tomato_today_time)
    TextView tomatoTodayTime;
    @BindView(R.id.tomato_today_rank)
    TextView tomatoTodayRank;
    int sumtime;
    int todaytime;
    @BindView(R.id.customView1)
    LineView customView1;
    String ri;
    String date;
    FocusTime focusTime;

    List<String> xValues = new ArrayList<>();   //x轴数据集合
    List<Integer> yValues = new ArrayList<>();

    Integer [] a = new Integer[30];

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, TomatoActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_tomato;
    }

    @Override
    protected void initViews() {
        User user = DbRequest.getCurrentUser(this);
        sumtime = DbRequest.getCurrentUserFocusTime(TomatoActivity.this, user);
        tomatoSumTime.setText("" + sumtime);
        todaytime = DbRequest.getCurrentUserTodayFocusTime(this, user);
        tomatoTodayTime.setText("" + todaytime);
        List<User> users = DbRequest.findUsers();
        final List<User> sortUsers = SortUtil.sort(users, this);
        for (int i = 0; i < sortUsers.size(); i++) {
            if (sortUsers.get(i).getId() == user.getId()) {
                i = i + 1;
                tomatoTodayRank.setText("" + i);
            }
        }
        final List<User> sortUsers2 = SortUtil.sortSumTime(users, this);
        for (int i = 0; i < sortUsers2.size(); i++) {
            if (sortUsers2.get(i).getId() == user.getId()) {
                i = i + 1;
                tomatoSumRank.setText("" + i);
            }
        }
        tomatoIvWhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TomatowhyDialog.showDialog(TomatoActivity.this);
            }
        });
        tomatoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                datePickerFragment.show(fragmentManager, "date");
            }
        });

        initdata(user);
        customView1.setXValues(xValues);
        customView1.setYValues(yValues);
    }

    public void initdata(User user) {
        String month = TimeUtil.formatMonth();
        for (int i = 1; i < 31; i++) {
            xValues.add(month + i);
        }
        List<FocusTime> focusTimes = user.getFocusTimes();
        List<FocusTime> recentMonthFocusTimes = new ArrayList<>();
        for (FocusTime focusTime : focusTimes) {
            if (focusTime.getDate().contains(month)) {
                recentMonthFocusTimes.add(focusTime);
                Log.d(TAG, "initdata: recentMonthFocusTimes.size()" + recentMonthFocusTimes.size());
            }
        }

        for (int i= 0 ; i < 30; i++) {
            a[i] = 0;
        }

        for (int i = 0; i<recentMonthFocusTimes.size(); i++) {
            date = recentMonthFocusTimes.get(i).getDate().substring(8,10);
            if (date.charAt(1) == 0){
               a[Integer.parseInt(date.substring(1,2))-1] = recentMonthFocusTimes.get(i).getTime();
            } else {
                a[Integer.parseInt(date)-1] = recentMonthFocusTimes.get(i).getTime();
            }
        }
        yValues = Arrays.asList(a);

        /*for (int i = 0; i < 30; i++) {
            int size = recentMonthFocusTimes.size();
            if (i<size) {
                focusTime = recentMonthFocusTimes.get(i);
                date = focusTime.getDate().substring(8,10);
            }
            if (i<10) {
                int a = i+1;
                if (date.equals("0"+a)) {
                    yValues.add(focusTime.getTime());
                    Log.d(TAG, "initdata: "+ focusTime.getTime());
                } else {
                    yValues.add(0);
                }
            } else {
                int a = i+1;
                if (date.equals(a)) {
                    yValues.add(focusTime.getTime());
                    Log.d(TAG, "initdata: "+ focusTime.getTime());
                } else {
                    yValues.add(0);
                }
            }

        }*/
    }

}
