package com.scy.android.tomatotaskdo.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scy.android.tomatotaskdo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskFragment extends BaseFragment {

    @BindView(R.id.task_recycler_view)
    RecyclerView taskRecyclerView;
    Unbinder unbinder;

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.fragment_task, null);
        unbinder = ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskRecyclerView.setLayoutManager(linearLayoutManager);
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
    }

}
