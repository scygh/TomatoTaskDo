package com.scy.android.tomatotaskdo.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddTaskActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    private static final String TAG = "AddTaskActivity";
    @BindView(R.id.iv_voive)
    ImageView ivVoice;
    @BindView(R.id.iv_add_ok)
    ImageView ivAddOk;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_priority)
    EditText etPriority;
    private String description,prioritystr;
    private boolean flag = false;
    Task oldTask;

    public static Intent getAddTaskAcivityIntent(Context context,String description,String prioity) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra("des", description);
        intent.putExtra("pri", prioity);
        return intent;
    }

    public static Intent getAddTaskAcivityIntent(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        description = intent.getStringExtra("des");
        prioritystr = intent.getStringExtra("pri");
        if (!TextUtils.isEmpty(description)) {
            etDescription.setText(description);
            etPriority.setText(prioritystr);
            flag = true;
            User user = DbRequest.getCurrentUser(this);
            oldTask = DbRequest.getCurrentUserTodayUnFinishSelectTask(AddTaskActivity.this,user,description);

        }
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPriority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prioritystr = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(AddTaskActivity.this,"语音输入", 4, permissions);
                if (checkPermission(AddTaskActivity.this,permissions)) {
                    initSpeech(AddTaskActivity.this);
                }
            }
        });
        ivAddOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存到数据库
                if (Apis.checkLogin(AddTaskActivity.this)) {
                        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(prioritystr)){
                            ToastHelper.showToast(AddTaskActivity.this,"任务内容不能为空", Toast.LENGTH_SHORT);
                            return;
                        }
                        if (!flag) {
                            User user = DbRequest.getCurrentUser(AddTaskActivity.this);
                            Task task = new Task();
                            task.setIsFinished("未完成");
                            task.settDescription(description);
                            task.setPriority(prioritystr);
                            task.setStartTime(TimeUtil.formatTaskTime());
                            task.setUser(user);
                            task.save();
                            user.update(user.getId());
                        } else {
                            //更新task
                            Log.d(TAG, "onClick: " + oldTask.gettDescription());
                            oldTask.setIsFinished("未完成");
                            oldTask.settDescription(description);
                            oldTask.setPriority(prioritystr);
                            oldTask.setStartTime(TimeUtil.formatTaskTime());
                            oldTask.update(oldTask.getId());
                            ToastHelper.showToast(AddTaskActivity.this,"修改成功", Toast.LENGTH_SHORT);
                            flag = false;
                        }
                } else {
                    ToastHelper.showToast(AddTaskActivity.this,"需要您登陆才能保存任务", Toast.LENGTH_SHORT);
                }
                finish();
                startActivity(MainActivity.getIntent(AddTaskActivity.this));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MainActivity.getIntent(AddTaskActivity.this));
    }



    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    //返回的result为识别后的汉字,直接赋值到TextView上即可
                    String result = parseVoice(recognizerResult.getResultString());
                    String exits = etDescription.getText().toString();
                    if (TextUtils.isEmpty(exits)) {
                        etDescription.setText(result);
                    } else {
                        etDescription.setText(exits+" , "+result);
                    }
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
        TextView txt = (TextView)mDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("");
    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }

    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    public static boolean checkPermission(Activity context, String[] perms) { return EasyPermissions.hasPermissions(context, perms); }
    public static void requestPermission(Activity context,String tip,int requestCode,String[] perms) { EasyPermissions.requestPermissions(context, tip,requestCode,perms); }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //ToastHelper.showToast(this,"用户授权成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastHelper.showToast(this,"用户授权失败", Toast.LENGTH_SHORT);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) { new AppSettingsDialog.Builder(this).build().show(); }

    }
}
