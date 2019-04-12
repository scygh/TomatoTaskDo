package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.adapter.FocusRecordRvAdapter;
import com.scy.android.tomatotaskdo.view.adapter.TaskRecordRvAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FocusRecordActivity extends BaseActivity {

    @BindView(R.id.focusRecord_rv)
    RecyclerView focusRecordRv;
    private FocusRecordRvAdapter mFocusRecordRvAdapter;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, FocusRecordActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_focus_record;
    }

    @Override
    protected void initViews() {
        focusRecordRv.setLayoutManager(new LinearLayoutManager(this));
        List<FocusTime> focusTimes = DbRequest.getCurrentUser(this).getFocusTimes();
        mFocusRecordRvAdapter = new FocusRecordRvAdapter(focusTimes,this);
        focusRecordRv.setAdapter(mFocusRecordRvAdapter);
    }

}
