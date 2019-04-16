package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends BaseActivity {


    @BindView(R.id.signout_button)
    Button signoutButton;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.signOut(SettingsActivity.this);
                startActivity(LoginActivity.getIntent(SettingsActivity.this));
                finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MainActivity.getIntent(SettingsActivity.this));
    }

}
