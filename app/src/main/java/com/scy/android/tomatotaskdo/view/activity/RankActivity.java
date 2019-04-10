package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scy.android.tomatotaskdo.R;

public class RankActivity extends BaseActivity{

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, RankActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initViews() {

    }
}
