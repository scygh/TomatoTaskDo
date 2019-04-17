package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.scy.android.tomatotaskdo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDdetailActivity extends BaseActivity {

    @BindView(R.id.mt_description)
    TextView mtDescription;
    @BindView(R.id.mt_priority)
    TextView tPriority;
    private String description, prioritystr;


    public static Intent getAddTaskAcivityIntent(Context context, String description, String prioity) {
        Intent intent = new Intent(context, TaskDdetailActivity.class);
        intent.putExtra("des", description);
        intent.putExtra("pri", prioity);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail_task;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        description = intent.getStringExtra("des");
        prioritystr = intent.getStringExtra("pri");

        mtDescription.setText(description);
        tPriority.setText(prioritystr);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
