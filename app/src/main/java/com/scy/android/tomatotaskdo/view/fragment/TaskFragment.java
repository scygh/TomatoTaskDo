package com.scy.android.tomatotaskdo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.manager.MyLinearLayoutManager;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.activity.AddTaskActivity;
import com.scy.android.tomatotaskdo.view.adapter.TaskFragmentRvAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskFragment extends BaseFragment{
    private static final String TAG = "TaskFragment";

    @BindView(R.id.task_recycler_view)
    RecyclerView taskRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.task_fb)
    FloatingActionButton taskFb;
    private TaskFragmentRvAdapter mTaskFragmentRvAdapter;
    private List<Task> mTasks;
    private User currentUser;

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.fragment_task, null);
        unbinder = ButterKnife.bind(this, rootView);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mActivity, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskRecyclerView.setLayoutManager(linearLayoutManager);
        mTaskFragmentRvAdapter = new TaskFragmentRvAdapter(init(), mActivity);
        taskRecyclerView.setAdapter(mTaskFragmentRvAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //拖拽的方法标记
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //滑动方向标记
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //通过makeMovementFlags方法将将方向标记进行组合，并将复合的值返回
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                mTaskFragmentRvAdapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mTaskFragmentRvAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddTaskActivity.getIntent(mActivity), ConstValues.ACTION_ADD);
            }
        });
        return rootView;
    }

    public List<Task> init() {
        if (Apis.checkLogin(mActivity)){
            currentUser = DbRequest.getCurrentUser(mActivity);
            mTasks = DbRequest.getCurrentUserTasks(mActivity,currentUser);
        } else {
            mTasks = new ArrayList<>();
        }
        return mTasks;
    }

    @Override
    public void initData() {
        super.initData();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstValues.ACTION_ADD && resultCode == mActivity.RESULT_OK) {
            init();
            mTaskFragmentRvAdapter.notifyDataSetChanged();
        }
    }
}
