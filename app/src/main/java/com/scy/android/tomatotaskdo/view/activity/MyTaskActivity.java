package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.SortUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.adapter.TaskFragmentRvAdapter;
import com.scy.android.tomatotaskdo.view.adapter.TaskRecordRvAdapter;
import com.scy.android.tomatotaskdo.view.fragment.DatePickerFragment;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTaskActivity extends BaseActivity {

    private static final String TAG = "MyTaskActivity";
    @BindView(R.id.mytask_date)
    ImageView mytaskDate;
    @BindView(R.id.mytask_rv)
    RecyclerView mytaskRv;
    private TaskRecordRvAdapter mTaskFragmentRvAdapter;
    private List<Task> todayTask;
    DatePickerFragment datePickerFragment;
    String selectdate;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MyTaskActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_my_task;
    }

    @Override
    protected void initViews() {
        mytaskRv.setLayoutManager(new LinearLayoutManager(this));
        todayTask = DbRequest.getCurrentUserTodayTasks(this,DbRequest.getCurrentUser(this));
        mTaskFragmentRvAdapter = new TaskRecordRvAdapter(todayTask ,this);
        mytaskRv.setAdapter(mTaskFragmentRvAdapter);
        datePickerFragment = DatePickerFragment.newInstance(new Date());
        mytaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                datePickerFragment.show(fragmentManager, "date");
            }
        });

        datePickerFragment.setOnDialogListener(new DatePickerFragment.OnDialogListener() {
            @Override
            public void onDialogClick(Date date) {
                //根据date 来更新列表
                selectdate = TimeUtil.formatMyTaskTime(date);
                update();
            }
        });
    }
    public void update() {
        List<Task> tt = DbRequest.getCurrentUserSelectdayTasks(MyTaskActivity.this,DbRequest.getCurrentUser(MyTaskActivity.this),selectdate);
        todayTask.clear();
        todayTask.addAll(tt);
        Log.d(TAG, "onDialogClick: " + selectdate + todayTask.size());
        mTaskFragmentRvAdapter.notifyDataSetChanged();
    }

}
