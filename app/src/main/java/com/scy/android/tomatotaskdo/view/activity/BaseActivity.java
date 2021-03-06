package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;

import org.litepal.LitePal;
import org.zackratos.ultimatebar.UltimateBar;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //设置沉浸式导航栏
        UltimateBar ultimateBar =new UltimateBar(this);
        ultimateBar.setImmersionBar();
        ButterKnife.bind(this);
        //拿到数据库对象
        db = LitePal.getDatabase();
        initViews();
    }

    protected abstract int getContentView();
    protected abstract void initViews();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastHelper.cancelToast();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
