package com.scy.android.tomatotaskdo.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;
import com.scy.android.tomatotaskdo.R;

import org.litepal.LitePal;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * 沈程阳
 * created by scy on 2019/4/2 11:41
 * 邮箱：1797484636@qq.com
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/robotothin.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //数据库初始化
        LitePal.initialize(this);
        Fresco.initialize(this);
        MobSDK.init(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5cb58ad8");

    }
}
