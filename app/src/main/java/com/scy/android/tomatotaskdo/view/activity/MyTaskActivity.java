package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scy.android.tomatotaskdo.R;

public class MyTaskActivity extends BaseActivity {

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MyTaskActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_my_task;
    }

    @Override
    protected void initViews() {

    }
}
